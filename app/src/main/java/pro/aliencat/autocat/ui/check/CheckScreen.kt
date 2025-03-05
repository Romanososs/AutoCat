package pro.aliencat.autocat.ui.check

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pro.aliencat.autocat.R
import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.common.LoadingState
import pro.aliencat.autocat.ui.components.ErrorDialog
import pro.aliencat.autocat.ui.components.LoadingDialog
import pro.aliencat.autocat.ui.components.PlateNumberView
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.CustomTheme
import pro.aliencat.autocat.ui.theme.CustomTypography

@Composable
fun CheckScreenRoot(
    viewModel: CheckViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CheckScreen(state) { action ->
        if (action is CheckAction.OnCheckClick) {
            viewModel.onAction(CheckAction.OnCheckClick { onBackClick() })
            return@CheckScreen
        }
        viewModel.onAction(action)
        when (action) {
            CheckAction.OnDrawClose, CheckAction.OnErrorDismiss -> onBackClick()
            else -> {}
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckScreen(state: CheckState, onAction: (CheckAction) -> Unit) {
    val letters = listOf("А", "В", "Е", "К", "М", "Н", "О", "Р", "С", "Т", "У", "Х")
    val numbers = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    val buttonsPadding = 6.dp
    val sheetState = rememberModalBottomSheetState()
    LaunchedEffect(state.loading) {
        when (state.loading) {
            LoadingState.Loading, is LoadingState.Error -> sheetState.hide()
            else -> {}
        }
    }
    if (state.loading is LoadingState.Loading)
        LoadingDialog {}
    if (state.loading is LoadingState.Error)
        ErrorDialog(state.loading.message) { onAction(CheckAction.OnErrorDismiss) }
    ModalBottomSheet(
        { onAction(CheckAction.OnDrawClose) },
        sheetState = sheetState,
        scrimColor = Color(0x01FFFFFF)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 16.dp)
        ) {
            Text("Check number", style = MaterialTheme.typography.titleMedium)//TODO
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                PlateNumberView(PlateNumber(state.number), CustomTheme.current.foregroundPlateColor)
                Card(
                    onClick = { onAction(CheckAction.OnCheckClick()) },
                    enabled = state.number.isNotBlank(),
                    colors = CardDefaults.cardColors().copy(
                        containerColor = ButtonDefaults.buttonColors().containerColor,
                        contentColor = ButtonDefaults.buttonColors().contentColor,
                        disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor
                    ),
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .fillMaxWidth()
                        .height(38.dp)
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { Text("Check", style = MaterialTheme.typography.titleMedium) }
                }//TODO
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(buttonsPadding),
                    modifier = Modifier.weight(1F)
                ) {
                    for (i in 0..<4)
                        Row(horizontalArrangement = Arrangement.spacedBy(buttonsPadding)) {
                            for (j in 0..<3) {
                                val letter = letters[i * 3 + j]
                                KeyboardButton(letter, modifier = Modifier.weight(1F)) {
                                    onAction(CheckAction.OnSymbolAdded(letter))
                                }
                            }
                        }
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(buttonsPadding),
                    modifier = Modifier.weight(1F)
                ) {
                    for (i in 0..<3)
                        Row(horizontalArrangement = Arrangement.spacedBy(buttonsPadding)) {
                            for (j in 0..<3) {
                                val num = numbers[i * 3 + j]
                                KeyboardButton(num, modifier = Modifier.weight(1F)) {
                                    onAction(CheckAction.OnSymbolAdded(num))
                                }
                            }
                        }
                    Row(horizontalArrangement = Arrangement.spacedBy(buttonsPadding)) {
                        KeyboardButton(numbers.last(), modifier = Modifier.weight(1F)) {
                            CheckAction.OnSymbolAdded(numbers.last())
                        }
                        Card(
                            onClick = { onAction(CheckAction.OnSymbolRemoved) },
                            colors = CardDefaults.cardColors().copy(
                                containerColor = CustomTheme.current.mainElement,
                                contentColor = ButtonDefaults.buttonColors().contentColor,
                                disabledContainerColor = CustomTheme.current.subElement
                            ),
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1F)
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(painterResource(R.drawable.backspace_24dp), null)//TODO
                            }
                        }
                        Card(
                            onClick = { onAction(CheckAction.OnCheckClick()) },
                            colors = CardDefaults.cardColors().copy(
                                containerColor = ButtonDefaults.buttonColors().containerColor,
                                contentColor = ButtonDefaults.buttonColors().contentColor,
                                disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor
                            ),
                            modifier = Modifier
                                .height(48.dp)
                                .weight(1F)
                        ) {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Icon(painterResource(R.drawable.keyboard_return_24dp), null)//TODO
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KeyboardButton(letter: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        onClick = onClick, modifier = modifier.height(48.dp)
    ) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                letter,
                style = CustomTypography.current.numberPlateLarge,
                fontSize = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CheckScreenPreview() {
    AutoCatTheme {

    }
}