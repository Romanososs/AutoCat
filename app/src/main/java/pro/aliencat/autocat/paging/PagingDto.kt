package pro.aliencat.autocat.paging

abstract class PagingDto<T> {
    abstract val count: Int?
    abstract val pageToken: String?
    abstract val items: List<T>
}
