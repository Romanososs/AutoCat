package pro.aliencat.autocat.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [VehicleEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun vehicleDao(): VehicleDao
}