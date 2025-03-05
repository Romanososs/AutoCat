package pro.aliencat.autocat.ui.screens.search

import pro.aliencat.autocat.paging.PagingState
import pro.aliencat.autocat.ui.screens.search.model.SearchFilter
import pro.aliencat.autocat.ui.screens.search.model.SearchResultSection

data class SearchState(
    val totalVehicles: Int = 0,
    val searchFilter: SearchFilter = SearchFilter(),

    val pagingState: PagingState = PagingState(),
    val vehicles: List<SearchResultSection> = emptyList(),
)
