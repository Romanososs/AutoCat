package pro.aliencat.autocat.ui.search.filter.list

//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import androidx.navigation.toRoute
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.onStart
//import kotlinx.coroutines.flow.stateIn
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import pro.aliencat.autocat.models.common.Result
//import pro.aliencat.autocat.repositories.VehicleRepository
//import pro.aliencat.autocat.ui.app.ListRoute
//import pro.aliencat.autocat.ui.app.ListRouteNavType
//import pro.aliencat.autocat.ui.app.Route
//import pro.aliencat.autocat.ui.screens.search.model.StringOption
//import pro.aliencat.autocat.ui.screens.search.model.StringOptionNavType
//import pro.aliencat.autocat.ui.screens.search.model.toStringOption
//import kotlin.math.max
//import kotlin.reflect.typeOf
//
//class FilterListViewModel(
//    savedStateHandle: SavedStateHandle,
//    private val vehicleRepository: VehicleRepository
//) : ViewModel() {
//    private val params = savedStateHandle.toRoute<Route.FilterList>(
//        mapOf(
//            typeOf<ListRoute>() to ListRouteNavType,
//            typeOf<StringOption>() to StringOptionNavType
//        )
//    )
//
//    private val _state = MutableStateFlow(FilterListState())
//    val state = _state
//        .onStart {
//            getList()
//        }
//        .stateIn(
//            viewModelScope,
//            SharingStarted.WhileSubscribed(5000L),
//            _state.value
//        )
//
//    fun onAction(action: FilterListAction) {
//        when (action) {
//            is FilterListAction.OnElementClick -> _state.update { it.copy(activeOption = action.index) }
//            else -> {}
//        }
//    }
//
//    private fun getList() {
//        viewModelScope.launch {
//            when (val route = params.type) {
//                ListRoute.BrandRoute -> updateList { vehicleRepository.getBrands() }
//                is ListRoute.ModelRoute -> if (route.brand is StringOption.Value) {
//                    when (val res = vehicleRepository.getModels(route.brand.value)) {
//                        is Result.Error -> {}//TODO
//                        is Result.Success -> _state.update { st ->
//                            st.copy(list = listOf(StringOption.Any) + res.data.map {
//                                it.toStringOption()
//                            })
//                        }
//                    }
//                }
//
//                ListRoute.ColorRoute -> updateList { vehicleRepository.getColors() }
//                ListRoute.YearRoute -> updateList { vehicleRepository.getYears() }
//            }
//
//            _state.update {
//                it.copy(
//                    activeOption = max(it.list.indexOf(params.activeElement), 0)
//                )
//            }
//        }
//    }
//
//    private suspend fun <T> updateList(getList: suspend () -> Result<List<T>>) {
//        when (val res = getList()) {
//            is Result.Error -> {}//TODO
//            is Result.Success -> _state.update { st ->
//                st.copy(list = listOf(StringOption.Any) + res.data.map {
//                    it.toString().toStringOption()
//                })
//            }
//        }
//    }
//
//}