package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleNameDto(
    val original: String? = null,
    val normalized: String? = null,
)