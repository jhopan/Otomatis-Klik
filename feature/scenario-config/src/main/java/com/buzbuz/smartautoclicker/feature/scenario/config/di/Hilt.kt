/*
 * Copyright (C) 2024 Kevin Buzeau
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
package com.buzbuz.smartautoclicker.feature.scenario.config.di

import com.buzbuz.smartautoclicker.core.ui.overlays.di.OverlayComponent
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.ActionTypeSelectionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.changecounter.ChangeCounterViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.click.ClickViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.copy.ActionCopyModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.IntentViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.activities.ActivitySelectionModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.component.ComponentSelectionModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.extras.ExtraConfigModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.intent.flags.FlagsSelectionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.pause.PauseViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.swipe.SwipeViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.toggleevent.EventTogglesViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.action.toggleevent.ToggleEventViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.common.counter.CounterNameSelectionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.common.intent.IntentActionsSelectionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.copy.ConditionCopyModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.image.CaptureViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.image.ImageConditionAreaSelectorViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.image.ImageConditionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.trigger.broadcast.BroadcastReceivedConditionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.trigger.counter.CounterReachedConditionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.condition.trigger.timer.TimerReachedConditionViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.EventDialogViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.actions.ActionsViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.conditions.ConditionsViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.config.EventConfigViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.event.copy.EventCopyModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.ScenarioDialogViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.config.ScenarioConfigViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.imageevents.ImageEventListViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.more.MoreViewModel
import com.buzbuz.smartautoclicker.feature.scenario.config.ui.scenario.triggerevents.TriggerEventListViewModel

import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn

@EntryPoint
@InstallIn(OverlayComponent::class)
interface ScenarioConfigViewModelsEntryPoint {

    fun actionCopyViewModel(): ActionCopyModel
    fun actionsViewModel(): ActionsViewModel
    fun actionTypeSelectionViewModel(): ActionTypeSelectionViewModel
    fun activitySelectionViewModel(): ActivitySelectionModel
    fun broadcastReceivedConditionViewModel(): BroadcastReceivedConditionViewModel
    fun captureViewModel(): CaptureViewModel
    fun changeCounterViewModel(): ChangeCounterViewModel
    fun clickViewModel(): ClickViewModel
    fun componentSelectionViewModel(): ComponentSelectionModel
    fun conditionCopyViewModel(): ConditionCopyModel
    fun conditionsViewModel(): ConditionsViewModel
    fun counterNameSelectionViewModel(): CounterNameSelectionViewModel
    fun counterReachedConditionViewModel(): CounterReachedConditionViewModel
    fun eventConfigViewModel(): EventConfigViewModel
    fun eventCopyModel(): EventCopyModel
    fun eventDialogViewModel(): EventDialogViewModel
    fun eventTogglesViewModel(): EventTogglesViewModel
    fun extraConfigViewModel(): ExtraConfigModel
    fun flagsSelectionViewModel(): FlagsSelectionViewModel
    fun imageConditionAreaSelectorViewModel(): ImageConditionAreaSelectorViewModel
    fun imageConditionViewModel(): ImageConditionViewModel
    fun imageEventListViewModel(): ImageEventListViewModel
    fun intentActionsSelectionViewModel(): IntentActionsSelectionViewModel
    fun intentViewModel(): IntentViewModel
    fun moreViewModel(): MoreViewModel
    fun pauseViewModel(): PauseViewModel
    fun scenarioConfigViewModel(): ScenarioConfigViewModel
    fun scenarioDialogViewModel(): ScenarioDialogViewModel
    fun swipeViewModel(): SwipeViewModel
    fun timerReachedConditionViewModel(): TimerReachedConditionViewModel
    fun toggleEventViewModel(): ToggleEventViewModel
    fun triggerEventListViewModel(): TriggerEventListViewModel
}