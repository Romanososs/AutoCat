package pro.aliencat.autocat.ui.search.filter.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pro.aliencat.autocat.R
import pro.aliencat.autocat.ui.app.ListRoute
import pro.aliencat.autocat.ui.search.SearchViewModel
import pro.aliencat.autocat.ui.search.model.StringOption
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.LightBlue

@Composable
fun FilterListScreenRoot(
    type: ListRoute,
    viewModel: SearchViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val filterListState by viewModel.filterListState.collectAsStateWithLifecycle()
    val activeOption = remember(state.searchFilter) {
        when (type) {
            ListRoute.BrandRoute -> state.searchFilter.brand
            ListRoute.ModelRoute -> state.searchFilter.model
            ListRoute.ColorRoute -> state.searchFilter.color
            ListRoute.YearRoute -> state.searchFilter.year
        }
    }
    val options = remember(filterListState) {
        when (type) {
            ListRoute.BrandRoute -> filterListState.brands
            ListRoute.ModelRoute -> filterListState.models
            ListRoute.ColorRoute -> filterListState.colors
            ListRoute.YearRoute -> filterListState.years
        }
    }

    FilterListScreen(activeOption, options) { action ->
        viewModel.onAction(action)
        when (action) {
            FilterListAction.OnBackClick -> onBackClick()
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterListScreen(
    activeOption: StringOption,
    options: List<StringOption>,
    onAction: (FilterListAction) -> Unit,
) {
    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = {}, navigationIcon = {
            IconButton({ onAction(FilterListAction.OnBackClick) }) {
                Icon(painterResource(R.drawable.arrow_back_24dp), null)//TODO
            }
        })
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Card(modifier = Modifier.padding(16.dp)) {
                options.forEachIndexed { index, item ->
                    //TODO "any" str
                    FilterListElement(
                        if (item is StringOption.Value) item.value else "Any",
                        item == activeOption
                    ) {
                        onAction(FilterListAction.OnElementClick(item))
                    }
                    if (options.size != index + 1) {
                        HorizontalDivider(thickness = 0.3.dp)
                    }
                }
            }
        }
    }
}

@Composable
fun FilterListElement(title: String, isCheck: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)
        if (isCheck) {
            Icon(painterResource(R.drawable.done_24dp), null, tint = LightBlue)//TODO
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun FilterListScreenPreview() {
    AutoCatTheme {
        FilterListScreen(
            StringOption.Any,
            options = listOf(
                StringOption.Any,
                StringOption.Any,
                StringOption.Any

            )
        ) { }
    }

}