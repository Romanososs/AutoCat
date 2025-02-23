package pro.aliencat.autocat.paging

sealed class PageLoadState {
    data object Idle: PageLoadState()
    data object Loading: PageLoadState()
    data object Error: PageLoadState() //mb make data class later
}