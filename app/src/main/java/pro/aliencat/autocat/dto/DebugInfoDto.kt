package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable

@Serializable
data class DebugInfoDto(
    val autocod: DebugInfoEntryDto = DebugInfoEntryDto(),
    val vin01vin: DebugInfoEntryDto = DebugInfoEntryDto(),
    val vin01base: DebugInfoEntryDto = DebugInfoEntryDto(),
    val vin01history: DebugInfoEntryDto = DebugInfoEntryDto(),
    val nomerogram: DebugInfoEntryDto = DebugInfoEntryDto(),
)

enum class DebugInfoStatus(val code: Int) {
    success(0),
    error(1),
    warning(2)
}
//fun getByCode(code: Int) = DebugInfoStatus.entries.first { it.code == code }

@Serializable
data class DebugInfoEntryDto(
    val fields: Long = 0,
    val error: String? = null,
    val status: Int = DebugInfoStatus.success.code
)
