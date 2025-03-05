package pro.aliencat.autocat.ui.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import pro.aliencat.autocat.network.SEARCH_PREFETCH_SIZE
import pro.aliencat.autocat.paging.LoadState
import pro.aliencat.autocat.paging.PagingLoadState
import pro.aliencat.autocat.ui.components.VehicleCellView
import pro.aliencat.autocat.ui.components.VehicleSearchBar
import pro.aliencat.autocat.ui.search.components.SearchScreenMenu
import pro.aliencat.autocat.ui.search.components.SearchScreenScopeBox
import pro.aliencat.autocat.ui.search.model.SearchFilter
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.CustomTheme

@Composable
fun SearchScreenRoot(
    viewModel: SearchViewModel = koinViewModel(),
    onFilterClick: () -> Unit,
    onMapClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SearchScreen(state) { action ->
        when (action) {
            SearchAction.OnFilterClick -> onFilterClick()
            SearchAction.OnMapClick -> onMapClick()
            else -> {}
        }
        viewModel.onAction(action)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
) {
    val appBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val searchScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val lazyColumnState = rememberLazyListState()
    val needAppend = remember {
        derivedStateOf {
            (lazyColumnState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: Int.MIN_VALUE) >= lazyColumnState.layoutInfo.totalItemsCount - SEARCH_PREFETCH_SIZE
        }
    }
    LaunchedEffect(needAppend.value) {
        if (needAppend.value) onAction(SearchAction.OnScrollToEndOfPage)
    }

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
            actions = {
                SearchScreenMenu(
                    { onAction(SearchAction.OnFilterClick) },
                    { onAction(SearchAction.OnMapClick) }) {
                    //TODO
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            scrollBehavior = appBarScrollBehavior
        )
        VehicleSearchBar(
            state.searchFilter.numberQuery,
            { onAction(SearchAction.OnSearchQueryChange(it)) },
            { onAction(SearchAction.OnSearchClick) },
            { onAction(SearchAction.OnCancelSearchClick) },
            scrollBehavior = if (state.searchFilter.numberQuery.isBlank()) searchScrollBehavior else appBarScrollBehavior,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp)
                .padding(horizontal = 8.dp)
        )
        AnimatedVisibility(state.searchFilter.numberQuery.isNotBlank()) {
            SearchScreenScopeBox(
                Modifier
                    .padding(bottom = 12.dp)
                    .padding(horizontal = 8.dp),
                appBarScrollBehavior
            ) {
                onAction(SearchAction.OnScopeBoxChange(it))
            }
        }

        PullToRefreshBox(
            isRefreshing = state.pagingState.refresh is LoadState.Loading,
            onRefresh = {
                onAction(SearchAction.OnPullToRefresh)
            },
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                state = lazyColumnState
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
                if (state.pagingState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
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
            if (state.pagingState.preload is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(
    showBackground = true, showSystemUi = true
)
@Composable
fun SearchScreenPreview() {
    AutoCatTheme {
        SearchScreen(
            SearchState(
                totalVehicles = 100,
                searchFilter = SearchFilter(numberQuery = ""),
                pagingState = PagingLoadState(preload = LoadState.Loading)
            )
        ) {}
    }
}
