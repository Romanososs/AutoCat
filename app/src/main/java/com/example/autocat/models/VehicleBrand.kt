package com.example.autocat.models

import kotlinx.serialization.Serializable

@Serializable
data class VehicleBrand(
    val name: String,
    val logo: String?,
)