package pro.aliencat.autocat.ui.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pro.aliencat.autocat.ui.components.VehicleCellView
import pro.aliencat.autocat.ui.components.vehicleDummy
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.CustomTheme

@Composable
fun HistoryScreenRoot(viewModel: HistoryViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HistoryScreen(state, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HistoryScreen(
    state: HistoryState,
    onAction: (HistoryAction) -> Unit
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val searchScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(appBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(searchScrollBehavior.nestedScrollConnection),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Found ${state.totalVehicles}",//TODO
                    style = MaterialTheme.typography.titleMedium
                )
            },
            actions = {},
            windowInsets = WindowInsets(0, 0, 0, 0),
            scrollBehavior = appBarScrollBehavior
        )
//        VehicleSearchBar(
//            state.searchFilter.numberQuery,
//            { onAction(SearchAction.OnSearchQueryChange(it)) },
//            { onAction(SearchAction.OnSearchClick) },
//            { onAction(SearchAction.OnCancelSearchClick) },
//            scrollBehavior = if (state.searchFilter.numberQuery.isBlank()) searchScrollBehavior else appBarScrollBehavior,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(bottom = 12.dp)
//                .padding(horizontal = 8.dp)
//        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            for (section in state.sections) {
                stickyHeader(section.key) {
                    Surface(modifier = Modifier.fillMaxWidth()) {
                        Column {
                            Text(
                                "${section.key} (${section.value.size})",
                                style = MaterialTheme.typography.titleMedium,
                                color = CustomTheme.current.mainElement,
                                modifier = Modifier.padding(
                                    horizontal = 16.dp,
                                    vertical = 4.dp
                                )
                            )
                            HorizontalDivider(
                                thickness = 0.3.dp,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
                for (vehicle in section.value) {
                    item(vehicle.id) { VehicleCellView(vehicle) }
                    if (section.value.last() == vehicle) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                                    .background(MaterialTheme.colorScheme.surface)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen(HistoryState()) {}
    AutoCatTheme {
        HistoryScreen(
            HistoryState(
                totalVehicles = 13,
                sections = mapOf("123" to listOf(vehicleDummy))
            )
        ) {}
    }
}
