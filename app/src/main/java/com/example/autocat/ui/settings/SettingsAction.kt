package com.example.autocat.ui.settings

sealed class SettingsAction {
    data class onLoginClick(val email: String, val password: String): SettingsAction()
    data object onLogoutClick: SettingsAction()
}