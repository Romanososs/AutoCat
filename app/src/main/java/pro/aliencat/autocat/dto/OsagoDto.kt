package pro.aliencat.autocat.dto

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class OsagoDto(
    val date: Double = 0.0,
    val number: String = "",
    val vin: String? = null,
    val plateNumber: String? = null,
    val name: String = "",
    val status: String? = null,
    val restrictions: String = "",
    val insurant: String? = null,
    val owner: String? = null,
    val usageRegion: String? = null,
    val birthday: String? = null,
)