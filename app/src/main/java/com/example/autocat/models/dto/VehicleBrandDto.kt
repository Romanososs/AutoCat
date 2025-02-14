package com.example.autocat.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleBrandDto(
    val name: VehicleNameDto? = null,
    val logo: String? = null,
)
