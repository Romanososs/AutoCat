package pro.aliencat.autocat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EmailAuthDto(
    @SerialName("_id")
    val id: String,
    val email: String,
    val token: String
)
