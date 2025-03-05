package pro.aliencat.autocat.ui.history

import pro.aliencat.autocat.models.Vehicle

data class HistoryState(
    val totalVehicles: Int = 0,
    val sections: Map<String, List<Vehicle>> = mapOf(),
)
