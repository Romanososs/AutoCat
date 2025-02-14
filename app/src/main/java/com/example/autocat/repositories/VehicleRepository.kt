package com.example.autocat.repositories

import com.example.autocat.mappers.toVehicle
import com.example.autocat.models.Vehicle
import com.example.autocat.models.common.Result
import com.example.autocat.network.ApiDataSource
import com.example.autocat.storage.PreferencesDataSource

enum class AddedBy{
    anyone,
    me,
    anyoneButMe
}

interface VehicleRepository {
    suspend fun getVehicles(addedBy: AddedBy): Result<Vehicle>
    suspend fun getReport(number: String): Result<Vehicle>
}

class VehicleRepositoryImpl(
    private val prefDataSource: PreferencesDataSource,
    private val apiDataSource: ApiDataSource
) : VehicleRepository {
    override suspend fun getVehicles(addedBy: AddedBy): Result<Vehicle> {
        TODO("Not yet implemented")
    }

    override suspend fun getReport(number: String): Result<Vehicle> {
        return apiDataSource.getReport(number).map {
            it.toVehicle()
        }
    }
}