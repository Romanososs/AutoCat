package pro.aliencat.autocat.paging

data class PagingLoadState(
    val refresh: LoadState = LoadState.Idle,
    val preload: LoadState = LoadState.Idle,
    val append: LoadState = LoadState.Idle
)