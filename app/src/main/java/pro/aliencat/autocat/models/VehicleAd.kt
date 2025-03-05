package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehicleAd(
    val id: Int,
    val url: String?,
    val price: String?,
    val date: Date,
    val mileage: String?,
    val region: String?,
    val city: String?,
    val adDescription: String?,
    val photos: List<String>,
)
