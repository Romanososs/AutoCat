package pro.aliencat.autocat.paging

data class PagingData<T>(
    val totalItems: Int = 0,
    val state: PagingState = PagingState(),
    val data: List<T> = emptyList()
)
