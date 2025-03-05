package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable

@Serializable
data class VehicleOwnershipPeriodDto(
    val lastOperation: String = "",
    val ownerType: String = "",
    val from: Long = 0,
    val to: Long = 0,
    val region: String? = null,
    val registrationRegion: String? = null,
    val locality: String? = null,
    val code: String? = null,
    val street: String? = null,
    val building: String? = null,
    val inn: String? = null,
)
