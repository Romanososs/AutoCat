package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleModelDto(
    val name: VehicleNameDto? = null
)

