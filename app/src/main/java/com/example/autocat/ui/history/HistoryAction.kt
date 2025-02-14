package com.example.autocat.ui.history

sealed class HistoryAction {
    data class onGetReportClick(val number: String): HistoryAction()
}