package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date

@Serializable
data class Vehicle(
    val id: String,
    val number: PlateNumber,
    val currentNumber: PlateNumber?,
    val vin1: String,
    val vin2: String?,
    val sts: String,
    val pts: String,
    val brand: VehicleBrand?,
    val model: VehicleModel?,
    val engine: VehicleEngine?,
    val color: String?,
    val year: Int?,
    val isRightWheel: Boolean?,
    val isJapanese: Boolean?,
    val addedBy: String?,
    val addedDate: Date,
    val updatedDate: Date?,

    val synchronized: Boolean = true,
){
    val unrecognized: Boolean = brand == null
    val outdated: Boolean = number != currentNumber

}
