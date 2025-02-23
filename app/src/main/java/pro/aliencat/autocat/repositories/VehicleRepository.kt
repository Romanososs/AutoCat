package pro.aliencat.autocat.repositories

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.aliencat.autocat.mappers.toVehicle
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.common.Result
import pro.aliencat.autocat.models.dto.VehiclePageDto
import pro.aliencat.autocat.network.ApiDataSource
import pro.aliencat.autocat.storage.PreferencesDataSource


interface VehicleRepository {
    suspend fun getLocaleVehiclesPage(): Result<VehiclePageDto>
//    fun getRemoteVehiclesFlow(
//        filter: SearchFilter = SearchFilter(),
//        getTotalCount: (Int) -> Unit = {}
//    ): Flow<PagingData<Vehicle>>

    suspend fun getReport(number: String): Result<Vehicle>
}

class VehicleRepositoryImpl(
    private val prefDataSource: PreferencesDataSource,
    private val apiDataSource: ApiDataSource
) : VehicleRepository {

    override suspend fun getLocaleVehiclesPage(): Result<VehiclePageDto> {
        return withContext(Dispatchers.IO) {
            TODO("Not yet implemented")
        }
    }

//    override fun getRemoteVehiclesFlow(
//        filter: SearchFilter,
//        getTotalCount: (Int) -> Unit
//    ):

    override suspend fun getReport(number: String): Result<Vehicle> {
        return withContext(Dispatchers.IO) {
            apiDataSource.getReport(number).map {
                it.toVehicle()
            }
        }
    }
}