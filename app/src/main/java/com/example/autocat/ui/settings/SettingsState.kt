package com.example.autocat.ui.settings

import com.example.autocat.models.User

data class SettingsState(
    //email == null -> user NOT logged in
    val email: String? = null // ну пока так

)
