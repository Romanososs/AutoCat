package pro.aliencat.autocat.repositories

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import pro.aliencat.autocat.database.VehicleDao
import pro.aliencat.autocat.database.toVehicleEntity
import pro.aliencat.autocat.mappers.toVehicle
import pro.aliencat.autocat.mappers.toVehicleEventDto
import pro.aliencat.autocat.mappers.toVehicleNote
import pro.aliencat.autocat.mappers.toVehicleNoteDto
import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.common.Result
import pro.aliencat.autocat.models.common.onSuccess
import pro.aliencat.autocat.network.ApiDataSource
import pro.aliencat.autocat.storage.PreferencesDataSource


interface VehicleRepository {
    fun getVehicleHistory(): Flow<List<Vehicle>>
    suspend fun getReport(number: String): Result<Vehicle>

    suspend fun startCheckVehicleTask(number: String, isUpdate: Boolean = false): Result<Vehicle>
    suspend fun checkVehicle(number: String, isUpdate: Boolean = false): Result<Vehicle>

    suspend fun getBrands(fetch: Boolean = false): Result<List<String>>
    suspend fun getModels(brand: String, fetch: Boolean = false): Result<List<String>>
    suspend fun getColors(fetch: Boolean = false): Result<List<String>>
    suspend fun getYears(fetch: Boolean = false): Result<List<Int>>
}

class VehicleRepositoryImpl(
    private val prefDataSource: PreferencesDataSource,
    private val vehicleDao: VehicleDao,
    private val apiDataSource: ApiDataSource,
    private val json: Json
) : VehicleRepository {
    private val scope = CoroutineScope(Dispatchers.IO)
    private var brands: List<String>? = null
    private var models: List<String>? = null
    private var colors: List<String>? = null
    private var years: List<Int>? = null

    override fun getVehicleHistory(): Flow<List<Vehicle>> = vehicleDao.getVehicles().map { list -> list.map { it.toVehicle(json) } }

    override suspend fun getReport(number: String): Result<Vehicle> = withContext(Dispatchers.IO) {
        apiDataSource.getReport(number).map {
            it.toVehicle()
        }
    }

    override suspend fun startCheckVehicleTask(number: String, isUpdate: Boolean): Result<Vehicle> {
        val def = scope.async { checkVehicle(number, isUpdate) }
        return def.await()
    }

    override suspend fun checkVehicle(number: String, isUpdate: Boolean): Result<Vehicle> =
        //FIXME мб убрать столько маппов
        withContext(Dispatchers.IO) {
            var vehicle = vehicleDao.getVehicleByNumber(number)?.toVehicle(json) ?: Vehicle(number)
            //TODO add locations
            val res = apiDataSource.checkVehicle(
                number,
                vehicle.notes.map { it.toVehicleNoteDto() },
                vehicle.events.map { it.toVehicleEventDto() },
                false
            ).map { it.toVehicle() }.onSuccess {
                vehicle = it
            }
            vehicleDao.insertVehicle(vehicle.toVehicleEntity(json))
            res
        }

    override suspend fun getBrands(fetch: Boolean): Result<List<String>> =
        withContext(Dispatchers.IO) {
            if (fetch) {
                apiDataSource.getBrands().also { if (it is Result.Success) brands = it.data }
            } else {
                brands?.let { Result.Success(it) } ?: apiDataSource.getBrands()
                    .also { if (it is Result.Success) brands = it.data }
            }
        }

    override suspend fun getModels(brand: String, fetch: Boolean): Result<List<String>> =
        withContext(Dispatchers.IO) {
            if (fetch) {
                apiDataSource.getModels(brand).also { if (it is Result.Success) models = it.data }
            } else {
                models?.let { Result.Success(it) } ?: apiDataSource.getModels(brand)
                    .also { if (it is Result.Success) models = it.data }
            }
        }

    override suspend fun getColors(fetch: Boolean): Result<List<String>> =
        withContext(Dispatchers.IO) {
            if (fetch) {
                apiDataSource.getColors().also { if (it is Result.Success) colors = it.data }
            } else {
                colors?.let { Result.Success(it) } ?: apiDataSource.getColors()
                    .also { if (it is Result.Success) colors = it.data }
            }
        }

    override suspend fun getYears(fetch: Boolean): Result<List<Int>> =
        withContext(Dispatchers.IO) {
            if (fetch) {
                apiDataSource.getYears().also { if (it is Result.Success) years = it.data }
            } else {
                years?.let { Result.Success(it) } ?: apiDataSource.getYears()
                    .also { if (it is Result.Success) years = it.data }
            }
        }
}