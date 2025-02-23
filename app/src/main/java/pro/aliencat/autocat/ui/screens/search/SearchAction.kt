package pro.aliencat.autocat.ui.screens.search

import pro.aliencat.autocat.ui.screens.search.model.SearchScope

sealed class SearchAction {
    data class OnSearchQueryChange(val query: String): SearchAction()
    data object OnSearchClick: SearchAction()
    data object OnCancelSearchClick: SearchAction()
    data object OnPullToRefresh: SearchAction()
    data class OnScopeBoxChange(val scope: SearchScope): SearchAction()
    data class OnVehicleClick(val number: String): SearchAction()
    data object OnFilterClick: SearchAction()
    data object OnMapClick: SearchAction()
    data object OnShareClick: SearchAction()
}