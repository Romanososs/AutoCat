package pro.aliencat.autocat.ui.settings

data class SettingsState(
    //email == null -> user NOT logged in
    val email: String? = null // ну пока так

)
