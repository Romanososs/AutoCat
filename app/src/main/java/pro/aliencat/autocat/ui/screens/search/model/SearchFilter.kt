package pro.aliencat.autocat.ui.screens.search.model

data class SearchFilter(
    val scope: SearchScope = SearchScope.plateNumber,
    val addedBy: AddedBy = AddedBy.anyone,
    val sortBy: SortBy = SortBy.updatedDate,
    val sortOrder: SortOrder = SortOrder.descending,
    val brand: String? = null,
    val model: String? = null,
    val color: String? = null,
    val year: String? = null,
    val region: Int? = null,
    val fromDate: Int? = null, //TODO make Date
    val toDate: Int? = null,
    val fromDateUpdated: Int? = null,
    val toDateUpdated: Int? = null,
    val fromLocationDate: Int? = null,
    val toLocationDate: Int? = null,
    val numberQuery: String = ""
//public var needReset: Bool = false
) {

    fun toParamMap(): MutableMap<String, String> {
        val map = mutableMapOf(
            "scope" to scope.name,
            "addedBy" to addedBy.name,
            "sortBy" to sortBy.name,
            "sortOrder" to sortOrder.name
        )
        brand?.let { map.put("brand", it) }
        model?.let { map.put("model", it) }
        color?.let { map.put("color", it) }
        year?.let { map.put("year", it) }
        region?.let { map.put("region", it.toString()) }
        fromDate?.let { map.put("fromDate", it.toString()) }
        toDate?.let { map.put("toDate", it.toString()) }
        fromDateUpdated?.let { map.put("fromDateUpdated", it.toString()) }
        toDateUpdated?.let { map.put("toDateUpdated", it.toString()) }
        fromLocationDate?.let { map.put("fromLocationDate", it.toString()) }
        toLocationDate?.let { map.put("toLocationDate", it.toString()) }
        return map
    }
}

enum class AddedBy {
    anyone,
    me,
    anyoneButMe
}

enum class SortBy {
    addedDate,
    updatedDate
}

enum class SortOrder {
    ascending,
    descending
}


enum class SearchScope(val text: String) {
    plateNumber("Plate Number"),
    vin("VIN"),
    notes("Notes")
}