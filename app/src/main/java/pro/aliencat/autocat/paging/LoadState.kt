package pro.aliencat.autocat.paging

sealed class LoadState {
    data object Idle: LoadState()
    data object Loading: LoadState()
    data object Error: LoadState() //mb make data class later
}