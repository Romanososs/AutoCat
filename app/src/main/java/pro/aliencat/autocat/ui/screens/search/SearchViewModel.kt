package pro.aliencat.autocat.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.insertSeparators
import androidx.paging.map
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pro.aliencat.autocat.mappers.toVehicle
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.network.SearchPagingSource
import pro.aliencat.autocat.repositories.VehicleRepository
import pro.aliencat.autocat.ui.screens.search.model.SearchUiElement

class SearchViewModel(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(SearchState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    //Не надо так >:-(
    private lateinit var lastSeparator: SearchUiElement.StickyHeader

    val searchResultState = Pager(
        PagingConfig(pageSize = 50, initialLoadSize = 50)
    ) {
//        println("TAGTAG create SearchPagingSource")
//        //TODO mb get factory from di?
//        SearchPagingSource(_state.value.searchFilter) { total ->
//            _state.update { it.copy(totalVehicles = total, isRefreshing = false) }
//        }
    }.flow.map { pagingData ->
        pagingData.map { dto -> dto.toVehicle() }.map { SearchUiElement.VehicleObj(it) }
            .insertSeparators { before, after ->
                if (after == null) return@insertSeparators null
                val afterDate = after.vehicle.updatedDate ?: after.vehicle.addedDate
                if (before == null) {
                    lastSeparator = SearchUiElement.StickyHeader(afterDate.toHeaderString())
                    return@insertSeparators lastSeparator
                }
                if (afterDate.toHeaderString() == lastSeparator.dateString) {
                    //Элементы в одном разделе
                    lastSeparator.count++
                    return@insertSeparators null
                } else {
                    lastSeparator = SearchUiElement.StickyHeader(afterDate.toHeaderString())
                    before.isLastInSection = true
                    return@insertSeparators lastSeparator
                }
            }
    }.cachedIn(viewModelScope)

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchFilter = it.searchFilter.copy(numberQuery = action.query)) }
            }

            SearchAction.OnSearchClick -> {}

            SearchAction.OnCancelSearchClick -> {
                _state.update { it.copy(searchFilter = it.searchFilter.copy(numberQuery = "")) }
            }

            SearchAction.OnPullToRefresh -> {
                _state.update { it.copy(isRefreshing = true) }
            }

            is SearchAction.OnScopeBoxChange -> {
                _state.update { it.copy(searchFilter = it.searchFilter.copy(scope = action.scope)) }
            }

            SearchAction.OnFilterClick -> {}
            SearchAction.OnMapClick -> {}
            SearchAction.OnShareClick -> {}

            is SearchAction.OnVehicleClick -> {}
        }
    }
}