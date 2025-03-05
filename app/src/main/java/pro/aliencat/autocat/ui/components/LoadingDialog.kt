package pro.aliencat.autocat.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import pro.aliencat.autocat.ui.theme.AutoCatTheme


@Composable
fun LoadingDialog(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest) {
        Card(modifier = Modifier.padding(16.dp))  {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }
    }
}

@Preview
@Composable
fun LoadingDialogPreview(){
    AutoCatTheme {
        LoadingDialog {  }
    }
}