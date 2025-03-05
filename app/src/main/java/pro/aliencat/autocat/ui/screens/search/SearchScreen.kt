package pro.aliencat.autocat.ui.screens.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.koin.compose.viewmodel.koinViewModel
import pro.aliencat.autocat.ui.components.VehicleCellView
import pro.aliencat.autocat.ui.components.VehicleSearchBar
import pro.aliencat.autocat.ui.screens.search.components.SearchScreenMenu
import pro.aliencat.autocat.ui.screens.search.components.SearchScreenScopeBox
import pro.aliencat.autocat.ui.screens.search.model.SearchFilter
import pro.aliencat.autocat.ui.screens.search.model.SearchUiElement
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.CustomTheme

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val searchResult = viewModel.searchResultState.collectAsLazyPagingItems()

    SearchScreen(state, searchResult, viewModel::onAction)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    state: SearchState,
    searchResult: LazyPagingItems<SearchUiElement>,
    onAction: (SearchAction) -> Unit
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val searchScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(appBarScrollBehavior.nestedScrollConnection)
            .nestedScroll(searchScrollBehavior.nestedScrollConnection)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    "Found ${state.totalVehicles}",//TODO
                    style = MaterialTheme.typography.titleMedium
                )
            },
            actions = { SearchScreenMenu() },
            scrollBehavior = appBarScrollBehavior
        )
        VehicleSearchBar(
            state.searchFilter.numberQuery,
            { onAction(SearchAction.OnSearchQueryChange(it)) },
            {
                onAction(SearchAction.OnSearchClick)
                //Вообще это не нрав
                searchResult.refresh()
            },
            { onAction(SearchAction.OnCancelSearchClick) },
            scrollBehavior = searchScrollBehavior
        )
        AnimatedVisibility(state.searchFilter.numberQuery.isNotBlank()) {
            SearchScreenScopeBox(Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
        }
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                onAction(SearchAction.OnPullToRefresh)
                searchResult.refresh()
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                if (searchResult.itemCount > 0)
                    for (i in 0..<searchResult.itemCount) {
                        when (val it = searchResult.peek(i)) {
                            is SearchUiElement.StickyHeader -> stickyHeader(it.dateString) {
                                Surface(modifier = Modifier.fillMaxWidth()) {
                                    Column {
                                        Text(
                                            "${it.dateString} (${it.count})",
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

                            is SearchUiElement.VehicleObj -> {
                                item(it.vehicle.id) { VehicleCellView((searchResult[i] as SearchUiElement.VehicleObj).vehicle) }
                                if (it.isLastInSection) {
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

                            null -> {}
                        }
                    }
                if (searchResult.loadState.append is LoadState.Loading) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(24.dp),
                                color = CustomTheme.current.subElement
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SearchScreenPreview() {
//    AutoCatTheme {
//        SearchScreen(
//            SearchState(
//                totalVehicles = 100,
//                searchFilter = SearchFilter(numberQuery = "123"),
//            ), TODO()
//        ) {}
//    }
//}

@Preview(showBackground = true)
@Composable
fun Preview() {
    AutoCatTheme {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp),
            color = CustomTheme.current.subElement
        )
    }
}
