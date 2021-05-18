package com.trendyol.devtools.internal.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trendyol.devtools.internal.domain.EnvironmentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

internal class MainViewModel(
    private val environments: List<String>,
    private val environmentUseCase: EnvironmentUseCase
) : ViewModel() {

    private val actionChannel = Channel<Action>(Channel.BUFFERED)
    val actionsFlow = actionChannel.receiveAsFlow()

    fun onEnvironmentChangeClicked() {
        viewModelScope.launch(Dispatchers.Default) {
            val currentEnvironment = environmentUseCase.getCurrentEnvironment()
            val environmentsPair = environments.map { (it == currentEnvironment) to it }

            actionChannel.send(Action.ShowEnvironmentSelection(environmentsPair))
        }
    }

    fun onEnvironmentSelected(environmentIndex: Int) {
        environmentUseCase.updateCurrentEnvironment(environments[environmentIndex])
    }

    sealed class Action {

        class ShowEnvironmentSelection(val environments: List<Pair<Boolean, String>>) : Action()
    }
}
