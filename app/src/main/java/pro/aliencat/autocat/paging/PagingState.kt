package pro.aliencat.autocat.paging

data class PagingState<T>(
    val totalItems: Int = 0,
    val state: PagingLoadState = PagingLoadState(),
    val pages: List<Page<T>> = emptyList()
)
