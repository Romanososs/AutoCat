package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.dto.DebugInfoStatus

@Serializable
data class DebugInfo(
    val autocod: DebugInfoEntry,
    val vin01vin: DebugInfoEntry,
    val vin01base: DebugInfoEntry,
    val vin01history: DebugInfoEntry,
    val nomerogram: DebugInfoEntry,
)

@Serializable
data class DebugInfoEntry(
    val fields: Long,
    val error: String?,
    val status: DebugInfoStatus
)
