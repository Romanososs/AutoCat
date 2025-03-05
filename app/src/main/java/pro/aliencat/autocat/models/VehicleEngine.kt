package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable

@Serializable
data class VehicleEngine(
    val number: String?,
    val volume: Int?,
    val powerHp: Float?,
    val powerKw: Float?,
    val fuelType: String?,
)
