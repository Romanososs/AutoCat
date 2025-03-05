package pro.aliencat.autocat.paging

data class PagingConfig(
    val pageSize: Int = 30,
    val prefetchSize: Int = 10
)
