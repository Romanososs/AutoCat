package pro.aliencat.autocat.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pro.aliencat.autocat.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository
): ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state
        .combine(userRepository.getUser()){ vmState, user ->
            vmState.copy(email = user.email)
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: SettingsAction) {
        when(action) {
            is SettingsAction.OnLoginClick -> viewModelScope.launch {
                userRepository.login(action.email, action.password)
            }
            SettingsAction.OnLogoutClick -> viewModelScope.launch {
                userRepository.logout()
            }
        }
    }

}