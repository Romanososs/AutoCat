package pro.aliencat.autocat.ui.screens.history

sealed class HistoryAction {
    data class OnGetReportClick(val number: String): HistoryAction()
}