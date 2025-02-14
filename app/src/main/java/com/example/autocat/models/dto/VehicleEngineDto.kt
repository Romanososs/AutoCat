package com.example.autocat.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleEngineDto(
    val number: String? = null,
    val volume: Int? = null,
    val powerHp: Float? = null,
    val powerKw: Float? = null,
    val fuelType: String? = null,
)