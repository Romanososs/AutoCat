package pro.aliencat.autocat.paging

data class PagingState(
    val refresh: PageLoadState = PageLoadState.NotLoading,
    val preload: PageLoadState = PageLoadState.NotLoading,
    val append: PageLoadState = PageLoadState.NotLoading
)