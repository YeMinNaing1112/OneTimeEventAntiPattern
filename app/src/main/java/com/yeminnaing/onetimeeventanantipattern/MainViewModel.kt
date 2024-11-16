package com.yeminnaing.onetimeeventanantipattern

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val navigationChannel = Channel<NavigationEvent>()
    val navigationChannelFlow = navigationChannel.receiveAsFlow()

    private val _navigationEvent = MutableSharedFlow<NavigationEvent>()
    val navigationEventShareFlow = _navigationEvent.asSharedFlow()

    var isLoggedIn by mutableStateOf(false)

    var state by mutableStateOf(LoginState())

    fun login() {
        viewModelScope.launch {
           state= state.copy(isLoading = true)
            delay(3000 )
            navigationChannel.send(NavigationEvent.NavigateToProfile)

            state=state.copy(
                isLoading = false
            )
        }
    }


}

sealed interface NavigationEvent {
    data object NavigateToProfile : NavigationEvent
}

data class LoginState(
    val isLoading:Boolean=false,
    val isLoggedIn:Boolean=false
)