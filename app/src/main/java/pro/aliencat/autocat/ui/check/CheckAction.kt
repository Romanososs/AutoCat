package pro.aliencat.autocat.ui.check

sealed class CheckAction {
    data object OnDrawClose : CheckAction()
    data object OnErrorDismiss : CheckAction()
    data object OnSymbolRemoved : CheckAction()
    data class OnSymbolAdded(val letter: String) : CheckAction()
    data class OnCheckClick(val onSuccess: (() -> Unit)? = null) : CheckAction()
}
