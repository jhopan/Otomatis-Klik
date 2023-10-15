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
package com.buzbuz.smartautoclicker.feature.scenario.config.dumb.ui.scenario

import android.app.Application

import androidx.lifecycle.AndroidViewModel

import com.buzbuz.smartautoclicker.core.dumb.domain.model.DumbAction
import com.buzbuz.smartautoclicker.core.dumb.domain.model.DumbScenario
import com.buzbuz.smartautoclicker.core.ui.bindings.dropdown.DropdownItem
import com.buzbuz.smartautoclicker.feature.scenario.config.dumb.R
import com.buzbuz.smartautoclicker.feature.scenario.config.dumb.domain.DumbEditionRepository
import com.buzbuz.smartautoclicker.feature.scenario.config.dumb.ui.scenario.actionlist.DumbActionDetails
import com.buzbuz.smartautoclicker.feature.scenario.config.dumb.ui.scenario.actionlist.toActionDetails

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take

class DumbScenarioViewModel(application: Application) : AndroidViewModel(application) {

    private val dumbEditionRepository = DumbEditionRepository.getInstance(application)

    private val userModifications: StateFlow<DumbScenario?> =
        dumbEditionRepository.editedDumbScenario

    /**
     * Tells if all content have their field correctly configured.
     * Used to display the red badge if indicating if there is something missing.
     */
    val navItemsValidity: Flow<Map<Int, Boolean>> = userModifications.map { dumbScenario ->
        buildMap {
            put(R.id.page_actions, dumbScenario?.dumbActions?.isNotEmpty() ?: false)
            put(R.id.page_config, dumbScenario?.name?.isNotEmpty() ?: false)
        }
    }

    /** Tells if the configured event is valid and can be saved. */
    val canBeSaved: Flow<Boolean> = userModifications.map { dumbScenario ->
        dumbScenario?.isValid() == true
    }

    /** The event name value currently edited by the user. */
    val scenarioName: Flow<String> =  userModifications
        .filterNotNull()
        .map { it.name }
        .take(1)
    /** Tells if the scenario name is valid or not. */
    val scenarioNameError: Flow<Boolean> = userModifications
        .map { it?.name?.isEmpty() == true }

    /** The number of times to repeat the scenario. */
    val repeatCount: Flow<String> = userModifications
        .filterNotNull()
        .map { it.repeatCount.toString() }
        .take(1)
    /** Tells if the repeat count value is valid or not. */
    val repeatCountError: Flow<Boolean> = userModifications
        .map { it == null || it.repeatCount <= 0 }
    /** Tells if the scenario should be repeated infinitely. */
    val repeatInfiniteState: Flow<Boolean> = userModifications
        .map { it == null || it.isRepeatInfinite }

    /** The maximum duration of the execution in minutes. */
    val maxDurationMin: Flow<String> = userModifications
        .filterNotNull()
        .map { it.maxDurationMin.toString() }
        .take(1)
    /** Tells if the repeat count value is valid or not. */
    val maxDurationMinError: Flow<Boolean> = userModifications
        .map { it == null || it.maxDurationMin <= 0 }
    /** Tells if there is no maximum duration. */
    val maxDurationMinInfiniteState: Flow<Boolean> = userModifications
        .map { it == null || it.isDurationInfinite }

    /** The list of dumb actions for the scenario. */
    val dumbActionsDetails: Flow<List<DumbActionDetails>> = userModifications
        .map { dumbScenario ->
            dumbScenario?.dumbActions?.map { it.toActionDetails(application) } ?: emptyList()
        }

    private val enabledRandomization = DropdownItem(
        title = R.string.dropdown_item_title_anti_detection_enabled,
        helperText = R.string.dropdown_helper_text_anti_detection_enabled,
    )
    private val disableRandomization = DropdownItem(
        title = R.string.dropdown_item_title_anti_detection_disabled,
        helperText = R.string.dropdown_helper_text_anti_detection_disabled,
    )
    val randomizationDropdownItems = listOf(enabledRandomization, disableRandomization)

    /** The randomization value for the scenario. */
    val randomization: Flow<DropdownItem> = userModifications
        .map {
            when (it?.randomize) {
                true -> enabledRandomization
                else -> disableRandomization
            }
        }
        .filterNotNull()

    fun setDumbScenarioName(name: String) {
        userModifications.value?.copy(name = name)?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun setRepeatCount(repeatCount: Int) {
        userModifications.value?.copy(repeatCount = repeatCount)?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun toggleInfiniteRepeat() {
        val currentValue = userModifications.value?.isRepeatInfinite ?: return
        userModifications.value?.copy(isRepeatInfinite = !currentValue)?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun setMaxDurationMinutes(durationMinutes: Int) {
        userModifications.value?.copy(maxDurationMin = durationMinutes)?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun toggleInfiniteMaxDuration() {
        val currentValue = userModifications.value?.isDurationInfinite ?: return
        userModifications.value?.copy(isDurationInfinite = !currentValue)?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun setRandomization(randomizationItem: DropdownItem) {
        userModifications.value?.let { scenario ->
            val value = when (randomizationItem) {
                enabledRandomization -> true
                disableRandomization -> false
                else -> return
            }

            dumbEditionRepository.updateDumbScenario(scenario.copy(randomize = value))
        }
    }

    fun updateDumbAction(dumbAction: DumbAction) {
        val actionList = userModifications.value?.dumbActions ?: return

        val actionIndex = actionList.indexOfFirst { it.id == dumbAction.id }
        if (actionIndex == -1) return

        userModifications.value?.copy(
            dumbActions = actionList.toMutableList().apply {
                set(actionIndex, dumbAction)
            }
        )?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun updateDumbActionOrder(actions: List<DumbActionDetails>) {
        userModifications.value?.copy(dumbActions = actions.map { it.action })?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }

    fun deleteDumbAction(dumbAction: DumbAction) {
        val actionList = userModifications.value?.dumbActions ?: return

        val actionIndex = actionList.indexOfFirst { it.id == dumbAction.id }
        if (actionIndex == -1) return

        userModifications.value?.copy(
            dumbActions = actionList.toMutableList().apply {
                removeAt(actionIndex)
            }
        )?.let {
            dumbEditionRepository.updateDumbScenario(it)
        }
    }
}

