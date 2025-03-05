package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehiclePhotoDto(
    val id: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val date: Double? = null,
    val url: String? = null,
)
