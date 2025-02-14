package com.example.autocat.models

import com.example.autocat.models.dto.VehicleBrandDto
import com.example.autocat.models.dto.VehicleEngineDto
import com.example.autocat.models.dto.VehicleModelDto
import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val number: String,
    val currentNumber: String,
    val vin1: String,
    val vin2: String,
    val sts: String,
    val pts: String,
    val brand: VehicleBrand?,
    val model: VehicleModel?,
    val engine: VehicleEngine?,
    val color: String?,
    val year: Int?,
    val isRightWheel: Boolean?,
    val isJapanese: Boolean?,
    val addedBy: String?,
)
