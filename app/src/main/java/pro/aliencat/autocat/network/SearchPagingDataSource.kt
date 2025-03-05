package pro.aliencat.autocat.network

import kotlinx.coroutines.flow.StateFlow
import pro.aliencat.autocat.mappers.toVehicle
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.dto.VehicleDto
import pro.aliencat.autocat.paging.PagingDataSource
import pro.aliencat.autocat.paging.PagingState
import pro.aliencat.autocat.ui.search.model.SearchFilter

const val SEARCH_PAGE_SIZE = 50
const val SEARCH_PREFETCH_SIZE = 10

interface SearchPagingDataSource {
    val pagingState: StateFlow<PagingState<Vehicle>>
    fun search(filter: SearchFilter)
    fun refresh()
    fun append()
}

class SearchPagingDataSourceImpl(
//    config: PagingConfig = PagingConfig(), //maybe.. some day...
    private val apiDataSource: ApiDataSource
) : SearchPagingDataSource, PagingDataSource<VehicleDto, Vehicle>() {
    private var lastFilter = SearchFilter()

    override fun search(filter: SearchFilter) {
        lastFilter = filter
        super.reload({ it.toVehicle() }) {
            apiDataSource.getVehiclesPage(filter, pageSize = SEARCH_PAGE_SIZE)
        }
    }

    override fun refresh() {
        super.refresh({ it.toVehicle() }) {
            apiDataSource.getVehiclesPage(lastFilter, pageSize = SEARCH_PAGE_SIZE)
        }
    }

    override fun append() {
        super.append({ it.toVehicle() }) {
            apiDataSource.getVehiclesPage(lastFilter, lastToken, SEARCH_PAGE_SIZE)
        }
    }

}