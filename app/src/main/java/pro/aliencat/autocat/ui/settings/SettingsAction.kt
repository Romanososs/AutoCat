package pro.aliencat.autocat.ui.settings

sealed class SettingsAction {
    data class OnLoginClick(val email: String, val password: String): SettingsAction()
    data object OnLogoutClick: SettingsAction()
}