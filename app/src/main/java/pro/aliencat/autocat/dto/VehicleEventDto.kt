package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehicleEventDto(
    val id: String,
    val date: Double = 0.0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val address: String? = null,
    val addedBy: String? = null,
    val number: String? = null
)

