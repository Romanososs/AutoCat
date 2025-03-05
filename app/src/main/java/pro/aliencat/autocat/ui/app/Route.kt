package pro.aliencat.autocat.ui.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object History: Route

    @Serializable
    data object Records: Route

    @Serializable
    data object Add: Route


    @Serializable
    data object Settings: Route

    @Serializable
    data object SearchGraph: Route

    @Serializable
    data object Search: Route

    @Serializable
    data object Filter: Route

    @Serializable
    data class FilterList(val type: ListRoute): Route

}

@Serializable
enum class ListRoute{
    BrandRoute,
    ModelRoute,
    ColorRoute,
    YearRoute
}

//@Serializable
//sealed class ListRoute {
//    @Serializable
//    data object BrandRoute: ListRoute()
//    @Serializable
//    data object ModelRoute: ListRoute()
//    @Serializable
//    data object ColorRoute: ListRoute()
//    @Serializable
//    data object YearRoute: ListRoute()
//}

//val ListRouteNavType = object : NavType<ListRoute>(
//    isNullableAllowed = false
//) {
//    override fun get(bundle: Bundle, key: String): ListRoute? =
//        bundle.getString(key)?.let { parseValue(it) }
//
//    override fun put(bundle: Bundle, key: String, value: ListRoute) {
//        bundle.putString(key, serializeAsValue(value))
//    }
//
//    override fun parseValue(value: String): ListRoute = Json.decodeFromString(value)
//
//    override fun serializeAsValue(value: ListRoute): String = Json.encodeToString(value)
//}
