package pro.aliencat.autocat.ui.screens.history

import pro.aliencat.autocat.models.Vehicle

data class HistoryState(
    val vehicles: List<String> = emptyList(),
    //TODO
    val report: Vehicle? = null,
    val isError: Boolean = false
)
