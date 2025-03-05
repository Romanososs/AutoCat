package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehicleEvent(
    val id: String,
    val date: Date,
    val latitude: Double,
    val longitude: Double,
    val address: String?,
    val addedBy: String?,
)