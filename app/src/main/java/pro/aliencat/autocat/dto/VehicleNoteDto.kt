package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date
import java.util.UUID

@Serializable
data class VehicleNoteDto(
    val id: String = UUID.randomUUID().toString(),
    val user: String = "",
    val date: Double = 0.0,
    val text: String = ""
)