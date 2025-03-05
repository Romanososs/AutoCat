package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehiclePhoto(
//    val brand: String?,
//    val model: String?,
//    val date: Date,
    val url: String,
)
