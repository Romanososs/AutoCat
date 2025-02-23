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
    data object Search: Route

    @Serializable
    data object Settings: Route
}
