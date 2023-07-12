/*
 * Copyright (C) 2023 Kevin Buzeau
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.buzbuz.smartautoclicker.feature.tutorial.data

import android.content.Context
import android.graphics.Rect

import com.buzbuz.smartautoclicker.core.ui.overlays.Overlay
import com.buzbuz.smartautoclicker.core.ui.overlays.manager.OverlayManager
import com.buzbuz.smartautoclicker.feature.tutorial.data.game.TutorialGameStateData
import com.buzbuz.smartautoclicker.feature.tutorial.data.monitoring.MonitoredViewsManager
import com.buzbuz.smartautoclicker.feature.tutorial.domain.model.game.TutorialGameTargetType

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
internal class TutorialPlayer(context: Context) {

    private val overlayManager: OverlayManager = OverlayManager.getInstance(context)
    private val monitoredViewsManager: MonitoredViewsManager = MonitoredViewsManager.getInstance()

    private val _tutorial: MutableStateFlow<TutorialData?> = MutableStateFlow(null)
    val tutorial: StateFlow<TutorialData?> = _tutorial

    private val stepIndex: MutableStateFlow<Int?> = MutableStateFlow(null)
    private var stepStartBackStackTop: MutableStateFlow<Overlay?> = MutableStateFlow(null)

    val gameState: Flow<TutorialGameStateData?> = _tutorial
        .flatMapLatest { it?.game?.gameState ?: flowOf(null) }

    val currentStep: Flow<TutorialStepData?> =
        combine(_tutorial, stepIndex, gameState, overlayManager.backStackTop) { tuto, index, gameState, top ->
            if (tuto == null || index == null) return@combine null
            if (index < 0 || index >= tuto.steps.size) return@combine null

            val step = tuto.steps[index]
            when (step.stepStartCondition) {
                StepStartCondition.Immediate ->
                    step

                StepStartCondition.NextOverlay ->
                    if (stepStartBackStackTop.value != top) step
                    else null

                StepStartCondition.GameWon ->
                    if (gameState?.isWon == true) step
                    else null

                StepStartCondition.GameLost ->
                    if (gameState?.isWon == false) step
                    else null
            }
        }

    val stepMonitoredViewPosition: Flow<Rect?> = currentStep
        .flatMapLatest { step ->
            if (step != null && step.stepEndCondition is StepEndCondition.MonitoredViewClicked)
                monitoredViewsManager.getViewPosition(step.stepEndCondition.type) ?: flowOf(null)
            else
                flowOf(null)
        }

    fun startTutorial(newTutorial: TutorialData) {
        // Keep track of current top of back stack value and monitored views
        setTutorialExpectedViews(newTutorial)
        stepStartBackStackTop.value = overlayManager.backStackTop.value

        stepIndex.value = 0
        _tutorial.value = newTutorial
    }

    fun nextStep() {
        val step = getCurrentStep() ?: return
        val index = stepIndex.value ?: return

        // Keep track of current top of back stack value
        stepStartBackStackTop.value = overlayManager.backStackTop.value

        when (val stepEndCondition = step.stepEndCondition) {
            StepEndCondition.NextButton -> {
                stepIndex.value = index + 1
            }

            is StepEndCondition.MonitoredViewClicked -> {
                monitoredViewsManager.performClick(stepEndCondition.type)
                stepIndex.value = index + 1
            }
        }
    }

    fun skipAllSteps() {
        stepIndex.value = null
        stepStartBackStackTop.value = null
    }

    fun startGame(scope: CoroutineScope, area: Rect, targetSize: Int) {
        _tutorial.value?.game?.start(scope, area, targetSize)
    }

    fun onGameTargetHit(target: TutorialGameTargetType) {
        _tutorial.value?.game?.onTargetHit(target)
    }

    fun stopGame() {
        _tutorial.value?.game?.stop()
    }

    fun stopTutorial() {
        _tutorial.value?.game?.stop()
        monitoredViewsManager.clearExpectedViews()
        stepIndex.value = null
        _tutorial.value = null
    }

    private fun getCurrentStep(): TutorialStepData? {
        val index = stepIndex.value ?: return null
        return _tutorial.value?.steps?.get(index)
    }

    private fun setTutorialExpectedViews(tutorial: TutorialData) {
        monitoredViewsManager.setExpectedViews(
            buildSet {
                tutorial.steps.forEach { step ->
                    if (step.stepEndCondition is StepEndCondition.MonitoredViewClicked)
                        add(step.stepEndCondition.type)
                }
            }
        )
    }
}