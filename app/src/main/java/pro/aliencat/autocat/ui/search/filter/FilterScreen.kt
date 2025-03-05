package pro.aliencat.autocat.ui.search.filter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pro.aliencat.autocat.R
import pro.aliencat.autocat.ui.app.ListRoute
import pro.aliencat.autocat.ui.search.SearchState
import pro.aliencat.autocat.ui.search.SearchViewModel
import pro.aliencat.autocat.ui.search.model.StringOption
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.CustomTheme

@Composable
fun FilterScreenRoot(
    viewModel: SearchViewModel,
    onMainFilterClick: (ListRoute) -> Unit,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    FilterScreen(state) { action ->
        viewModel.onAction(action)
        when (action) {
            FilterAction.OnBackClick -> onBackClick()
            FilterAction.OnBrandClick -> onMainFilterClick(ListRoute.BrandRoute)
            FilterAction.OnColorClick -> onMainFilterClick(ListRoute.ColorRoute)
            FilterAction.OnModelClick -> onMainFilterClick(ListRoute.ModelRoute)
            FilterAction.OnYearClick -> onMainFilterClick(ListRoute.YearRoute)
            else -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    state: SearchState,
    onAction: (FilterAction) -> Unit
) {

    Column(Modifier.fillMaxSize()) {
        TopAppBar(title = {}, navigationIcon = {
            IconButton({ onAction(FilterAction.OnBackClick) }) {
                Icon(painterResource(R.drawable.arrow_back_24dp), null)//TODO
            }
        })
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text(
                "Main filters",
                color = CustomTheme.current.mainElement,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 8.dp, bottom = 4.dp)
            )//TODO res
            Card {
                MainFilterElement(
                    "Brand",
                    if (state.searchFilter.brand is StringOption.Value)
                        state.searchFilter.brand.value
                    else "Any"
                ) {
                    onAction(FilterAction.OnBrandClick)
                }//TODO
                HorizontalDivider(thickness = 0.3.dp)
                MainFilterElement(
                    "Model",
                    if (state.searchFilter.model is StringOption.Value)
                        state.searchFilter.model.value
                    else "Any"
                ) {
                    onAction(FilterAction.OnModelClick)
                }
                HorizontalDivider(thickness = 0.3.dp)
                MainFilterElement(
                    "Color",
                    if (state.searchFilter.color is StringOption.Value)
                        state.searchFilter.color.value
                    else "Any"
                ) {
                    onAction(FilterAction.OnColorClick)
                }
                HorizontalDivider(thickness = 0.3.dp)
                MainFilterElement(
                    "Year",
                    if (state.searchFilter.year is StringOption.Value)
                        state.searchFilter.year.value
                    else "Any"
                ) {
                    onAction(FilterAction.OnYearClick)
                }
            }
        }
    }
}

@Composable
fun MainFilterElement(title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)
        Row {
            Text(value, color = CustomTheme.current.mainElement)
            Icon(
                painterResource(R.drawable.navigate_next_24dp),
                null,
                tint = CustomTheme.current.subElement
            )
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun FilterScreenPreview() {
    AutoCatTheme {
        FilterScreen(SearchState()) { }
    }
}