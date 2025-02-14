package com.example.autocat.mappers

import com.example.autocat.models.Vehicle
import com.example.autocat.models.VehicleBrand
import com.example.autocat.models.VehicleEngine
import com.example.autocat.models.VehicleModel
import com.example.autocat.models.dto.VehicleBrandDto
import com.example.autocat.models.dto.VehicleDto
import com.example.autocat.models.dto.VehicleEngineDto
import com.example.autocat.models.dto.VehicleModelDto

fun VehicleDto.toVehicle() = Vehicle(
    number,
    currentNumber ?: "",
    vin1 ?: "",
    vin2 ?: "",
    sts ?: "",
    pts ?: "",
    brand?.toVehicleBrand(),
    model?.toVehicleModel(),
    engine?.toVehicleEngine(),
    color,
    year,
    isRightWheel,
    isJapanese,
    addedBy
)

fun VehicleBrandDto.toVehicleBrand() = VehicleBrand(
    name?.normalized ?: "",
    logo
)

fun VehicleEngineDto.toVehicleEngine(): VehicleEngine? {
    if (number?.isBlank() != false &&
        volume == null &&
        powerHp == null &&
        powerKw == null &&
        fuelType?.isBlank() != false)
        return null
    return VehicleEngine(
        number,
        volume,
        powerHp,
        powerKw,
        fuelType
    )
}

fun VehicleModelDto.toVehicleModel() = VehicleModel(
    name?.normalized ?: ""
)