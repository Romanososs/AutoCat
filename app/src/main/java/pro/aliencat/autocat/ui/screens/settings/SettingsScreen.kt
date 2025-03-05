package pro.aliencat.autocat.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreenRoot(
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(state, viewModel::onAction)
}

@Composable
fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    var email by rememberSaveable { mutableStateOf("Niki6800@gmail.com") }
    var password by rememberSaveable { mutableStateOf("Qwerty123") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (state.email == null) {
            TextField(email, onValueChange = { email = it }, modifier = Modifier.fillMaxWidth())
            TextField(password, onValueChange = { password = it }, modifier = Modifier.fillMaxWidth())
        } else {
            TextField(state.email, onValueChange = {}, enabled = false,  modifier = Modifier.fillMaxWidth())
        }
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
            if (state.email == null)
                onAction(SettingsAction.OnLoginClick(email, password))
            else
                onAction(SettingsAction.OnLogoutClick)
        }) {
            Text(if (state.email == null) "Login" else "Logout")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen(SettingsState("Test")) {}
}