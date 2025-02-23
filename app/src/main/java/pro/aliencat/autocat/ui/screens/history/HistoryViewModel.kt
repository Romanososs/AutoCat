package pro.aliencat.autocat.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pro.aliencat.autocat.models.common.onError
import pro.aliencat.autocat.models.common.onSuccess
import pro.aliencat.autocat.repositories.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            fetchVehicles()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: HistoryAction) {
        when (action) {
            is HistoryAction.OnGetReportClick -> fetchReport(action.number)
        }
    }

    private fun fetchVehicles() = viewModelScope.launch {
        println("TAGTAG call fetchVehicles")
    }

    private fun fetchReport(number: String) = viewModelScope.launch {
        vehicleRepository.getReport(number)
            .onSuccess { res ->
                _state.update { it.copy(report = res, isError = false) }
            }
            .onError { _, _ ->
                _state.update { it.copy(isError = true) }
            }
    }
}