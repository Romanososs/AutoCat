package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehicleAdDto(
    val id: Int = 0,
    val url: String? =null,
    val price: String? =null,
    val date: Double = 0.0,
    val mileage: String? =null,
    val region: String? =null,
    val city: String? =null,
    val adDescription: String? =null,
    val photos: List<String> = emptyList(),
)