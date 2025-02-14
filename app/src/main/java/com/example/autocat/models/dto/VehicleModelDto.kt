package com.example.autocat.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleModelDto(
    val name: VehicleNameDto? = null
)

