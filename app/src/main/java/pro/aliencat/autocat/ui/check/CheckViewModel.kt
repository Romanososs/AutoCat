package pro.aliencat.autocat.ui.check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pro.aliencat.autocat.models.common.LoadingState
import pro.aliencat.autocat.models.common.onError
import pro.aliencat.autocat.models.common.onSuccess
import pro.aliencat.autocat.repositories.VehicleRepository

class CheckViewModel(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CheckState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: CheckAction) {
        when (action) {
            CheckAction.OnSymbolRemoved -> _state.update { it.copy(number = it.number.dropLast(1)) }
            is CheckAction.OnSymbolAdded -> if (_state.value.number.length < 9) _state.update {
                it.copy(number = it.number + action.letter)
            }

            is CheckAction.OnCheckClick -> if (_state.value.number.isNotBlank()) {
                _state.update { it.copy(loading = LoadingState.Loading) }
                viewModelScope.launch {
                    vehicleRepository.startCheckVehicleTask(_state.value.number)
                        .onSuccess { action.onSuccess?.invoke() }
                        .onError { _, msg ->
                            _state.update {
                                it.copy(loading = LoadingState.Error(msg))
                            }
                        }
                }
            }

            else -> {}
        }
    }
}