package pro.aliencat.autocat.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pro.aliencat.autocat.R
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.BrightRed

@Composable
fun ErrorDialog(message: String?, onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest) {
        Card(modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()) {//TODO
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Icon(
                    painterResource(R.drawable.error_24dp),
                    null,
                    tint = BrightRed,
                    modifier = Modifier.size(48.dp)
                )
                Text("Error", style = MaterialTheme.typography.titleLarge)
                Text(
                    message ?: stringResource(R.string.common_error),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(onClick = onDismissRequest) { Text("OK") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ErrorDialogPreview() {
    AutoCatTheme {
        ErrorDialog("Some error") { }
    }
}