package pro.aliencat.autocat.ui.search.model

import kotlinx.serialization.Serializable

data class SearchFilter(
    val scope: SearchScope = SearchScope.plateNumber,
    val addedBy: AddedBy = AddedBy.anyone,
    val sortBy: SortBy = SortBy.updatedDate,
    val sortOrder: SortOrder = SortOrder.descending,
    val brand: StringOption = StringOption.Any,
    val model: StringOption = StringOption.Any,
    val color: StringOption = StringOption.Any,
    val year: StringOption = StringOption.Any,
//    val region: List<Int>? = null,
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
        if (brand is StringOption.Value) map["brand"] = brand.value
        if (model is StringOption.Value) map["model"] = model.value
        if (color is StringOption.Value) map["color"] = color.value
        if (year is StringOption.Value) map["year"] = year.value
//        region?.let { map.put("region", it.toString()) }
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

@Serializable
sealed class StringOption{
    @Serializable
    data object Any: StringOption()
    @Serializable
    data class Value(val value: String): StringOption()
}

fun String.toStringOption() = StringOption.Value(this)
