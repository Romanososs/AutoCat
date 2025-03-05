package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class VehicleNote(
    val id: String,
    val user: String,
    val date: Date,
    val text: String,
)