package com.example.autocat.ui.history

import com.example.autocat.models.Vehicle

data class HistoryState(
    val vehicles: List<String> = emptyList(),
    //TODO
    val report: Vehicle? = null,
    val isError: Boolean = false
)
