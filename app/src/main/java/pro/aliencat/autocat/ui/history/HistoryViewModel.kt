package pro.aliencat.autocat.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import pro.aliencat.autocat.repositories.VehicleRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HistoryViewModel(
    private val vehicleRepository: VehicleRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HistoryState())
    val state = _state
        .onStart {
            observeVehicles()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: HistoryAction) {

    }

    private fun observeVehicles() {
        vehicleRepository.getVehicleHistory().onEach { f1 ->
            _state.update { st ->
                st.copy(
                    totalVehicles = f1.size,
                    sections = f1.sortedByDescending { it.updatedDate?.millisec() ?: it.addedDate.millisec() }
                        .groupBy { (it.updatedDate ?: it.addedDate).toHeaderString() }
                )
            }
        }.launchIn(viewModelScope)
    }
}