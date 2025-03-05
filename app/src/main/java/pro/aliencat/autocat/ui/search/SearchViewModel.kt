package pro.aliencat.autocat.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pro.aliencat.autocat.models.common.Result
import pro.aliencat.autocat.models.common.onError
import pro.aliencat.autocat.models.common.onSuccess
import pro.aliencat.autocat.network.SearchPagingDataSource
import pro.aliencat.autocat.repositories.VehicleRepository
import pro.aliencat.autocat.ui.app.ListRoute
import pro.aliencat.autocat.ui.search.filter.FilterAction
import pro.aliencat.autocat.ui.search.filter.list.FilterListAction
import pro.aliencat.autocat.ui.search.filter.list.MainFilterInfoState
import pro.aliencat.autocat.ui.search.model.SearchFilter
import pro.aliencat.autocat.ui.search.model.StringOption
import pro.aliencat.autocat.ui.search.model.toStringOption

class SearchViewModel(
    private val vehicleRepository: VehicleRepository,
    private val searchDataSource: SearchPagingDataSource,
) : ViewModel() {
    private var currentListOpened: ListRoute? = null
    private var needNewModels: Boolean = false

    private val _filterListState = MutableStateFlow(MainFilterInfoState())
    val filterListState = _filterListState
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _filterListState.value
        )

    private val _state = MutableStateFlow(SearchState())
    val state = _state
        .onStart {
            observeSearchState()
            println("TAGTAG create SearchViewModel")
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnSearchQueryChange -> {
                _state.update { it.copy(searchFilter = it.searchFilter.copy(numberQuery = action.query)) }
            }

            SearchAction.OnSearchClick -> {
                searchDataSource.search(_state.value.searchFilter)
            }

            SearchAction.OnCancelSearchClick -> {
                _state.update { it.copy(searchFilter = it.searchFilter.copy(numberQuery = "")) }
            }

            SearchAction.OnPullToRefresh -> {
                searchDataSource.refresh()
            }

            SearchAction.OnScrollToEndOfPage -> {
                searchDataSource.append()
            }

            is SearchAction.OnScopeBoxChange -> {
                _state.update { it.copy(searchFilter = it.searchFilter.copy(scope = action.scope)) }
                searchDataSource.search(_state.value.searchFilter)
            }

            SearchAction.OnFilterClick -> {
                loadFilterInfo()
            }

            SearchAction.OnMapClick -> {}
            SearchAction.OnShareClick -> {}

            is SearchAction.OnVehicleClick -> {}

        }
    }

    fun onAction(action: FilterAction) {
        when (action) {
            is FilterAction.OnAddedByClick -> _state.update {
                it.copy(
                    searchFilter = it.searchFilter.copy(
                        addedBy = action.newValue
                    )
                )
            }

            FilterAction.OnDoneClick -> searchDataSource.search(_state.value.searchFilter)
            FilterAction.OnBackClick -> {}
            FilterAction.OnBrandClick -> currentListOpened = ListRoute.BrandRoute
            FilterAction.OnColorClick -> currentListOpened = ListRoute.ColorRoute
            FilterAction.OnModelClick -> currentListOpened = ListRoute.ModelRoute
            FilterAction.OnYearClick -> currentListOpened = ListRoute.YearRoute
        }
    }

    fun onAction(action: FilterListAction) {
        when (action) {
            FilterListAction.OnBackClick -> {
                currentListOpened = null
                if (needNewModels) {
                    loadModels()
                    needNewModels = false
                }
            }

            is FilterListAction.OnElementClick -> {
                when (currentListOpened) {
                    ListRoute.BrandRoute -> _state.update {
                        needNewModels = true
                        it.copy(
                            searchFilter = it.searchFilter.copy(brand = action.item)
                        )
                    }

                    ListRoute.ModelRoute -> _state.update {
                        it.copy(
                            searchFilter = it.searchFilter.copy(model = action.item)
                        )
                    }

                    ListRoute.ColorRoute -> _state.update {
                        it.copy(
                            searchFilter = it.searchFilter.copy(color = action.item)
                        )
                    }

                    ListRoute.YearRoute -> _state.update {
                        it.copy(
                            searchFilter = it.searchFilter.copy(year = action.item)
                        )
                    }

                    null -> {}
                }
            }
        }
    }

    private fun observeSearchState() {
        searchDataSource.search(SearchFilter())
        searchDataSource.pagingState.onEach { f1 ->
            _state.update { st ->
                st.copy(
                    totalVehicles = f1.totalItems,
                    pagingState = f1.state,
                    //Было бы неплохо отправлять во flow по одной страницы и добавлять в секшены полученные страницы,
                    // вместо того, чтобы мапить и группировать все записи,
                    // но тогда надо менеджить два стейта.
                    // Есть ошибка, что flow коллектиться быстрее stateFlow
                    sections = f1.pages.map { it.data }.flatten().groupBy {
                        (it.updatedDate ?: it.addedDate).toHeaderString()
                    })
            }
        }.launchIn(viewModelScope)
    }

    private fun loadFilterInfo() {
        viewModelScope.launch {
            vehicleRepository.getBrands(true)
                .onSuccess {
                    _filterListState.update { st ->
                        st.copy(brands = listOf(StringOption.Any) + it.map { it.toStringOption() })
                    }
                }
                .onError { errorType, s -> }//TODO
        }
        viewModelScope.launch {
            vehicleRepository.getColors(true)
                .onSuccess {
                    _filterListState.update { st ->
                        st.copy(colors = listOf(StringOption.Any) + it.map { it.toStringOption() })
                    }
                }
                .onError { errorType, s -> }//TODO
        }

        viewModelScope.launch{
            vehicleRepository.getYears(true)
                .onSuccess {
                    _filterListState.update { st ->
                        st.copy(years = listOf(StringOption.Any) + it.map {
                            it.toString().toStringOption()
                        })
                    }
                }
                .onError { errorType, s -> }//TODO
        }
        loadModels()
    }

    private fun loadModels() {
        viewModelScope.launch {
            val brand = _state.value.searchFilter.brand
            if (brand !is StringOption.Any) {
                vehicleRepository.getModels((brand as StringOption.Value).value, true)
                    .onSuccess {
                        _filterListState.update { st ->
                            st.copy(models = listOf(StringOption.Any) + it.map { it.toStringOption() })
                        }
                    }
                    .onError { errorType, s -> }//TODO
            }
        }
    }
}