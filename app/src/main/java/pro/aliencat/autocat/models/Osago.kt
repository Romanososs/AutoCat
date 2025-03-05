package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class Osago(
    val date: Date,
    val number: String,
    val vin: String?,
    val plateNumber: String?,
    val name: String,
    val status: String?,
    val restrictions: String,
    val insurant: String?,
    val owner: String?,
    val usageRegion: String?,
    val birthday: String?
)
