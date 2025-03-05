package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable

@Serializable
data class VehicleBrand(
    val name: String?,
    val fullName: String?,
    val logo: String?,
)