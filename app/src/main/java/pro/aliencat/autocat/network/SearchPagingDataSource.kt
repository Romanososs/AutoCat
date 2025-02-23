package pro.aliencat.autocat.network

import kotlinx.coroutines.flow.StateFlow
import pro.aliencat.autocat.mappers.toVehicle
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.dto.VehicleDto
import pro.aliencat.autocat.paging.PagingData
import pro.aliencat.autocat.paging.PagingDataSource
import pro.aliencat.autocat.ui.screens.search.model.SearchFilter

interface SearchPagingDataSource {
    val pagingState: StateFlow<PagingData<Vehicle>>
    fun search(filter: SearchFilter)
    fun refresh()
    fun append()
}

class SearchPagingDataSourceImpl(
    private val pageSize: Int = 50,//TODO
    private val apiDataSource: ApiDataSource
) : SearchPagingDataSource, PagingDataSource<VehicleDto, Vehicle>() {
    private var lastFilter = SearchFilter()

    override fun search(filter: SearchFilter) {
        lastFilter = filter
        super.reload({ it.toVehicle() }) {
            apiDataSource.getVehiclesPage(filter)
        }
    }

    override fun refresh() {
        super.refresh({ it.toVehicle() }) {
            apiDataSource.getVehiclesPage(lastFilter)
        }
    }

    override fun append() {
        super.append({ it.toVehicle() }) {
            apiDataSource.getVehiclesPage(lastFilter)
        }
    }

}