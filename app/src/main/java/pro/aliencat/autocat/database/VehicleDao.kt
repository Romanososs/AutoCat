package pro.aliencat.autocat.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pro.aliencat.autocat.models.Vehicle

@Dao
interface VehicleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVehicle(vararg vehicle: VehicleEntity)

    @Query("SELECT * FROM vehicleentity WHERE number = :number")
    fun getVehicleByNumber(number: String): VehicleEntity?

    @Query("SELECT * FROM vehicleentity")
    fun getVehicles(): Flow<List<VehicleEntity>>

    @Delete
    fun deleteVehicle(vararg vehicle: VehicleEntity)
}