package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable

@Serializable
data class VehicleOwnershipPeriod(
    val lastOperation: String,
    val ownerType: String,
    val from: Long,
    val to: Long,
    val region: String?,
    val registrationRegion: String?,
    val locality: String?,
    val code: String?,
    val street: String?,
    val building: String?,
    val inn: String?,
)