package com.example.autocat.ui.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object History: Route

    @Serializable
    data object Settings: Route
}
