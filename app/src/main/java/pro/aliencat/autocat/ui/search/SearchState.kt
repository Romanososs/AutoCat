package pro.aliencat.autocat.ui.search

import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.paging.PagingLoadState
import pro.aliencat.autocat.ui.search.model.SearchFilter

data class SearchState(
    val totalVehicles: Int = 0,
    val searchFilter: SearchFilter = SearchFilter(),

    val pagingState: PagingLoadState = PagingLoadState(),
    val sections: Map<String, List<Vehicle>> = mapOf(),
)
