package pro.aliencat.autocat.models

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.models.common.Date
import java.util.Calendar
import java.util.UUID

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
    val photos: List<VehiclePhoto>,
    val ownershipPeriods: List<VehicleOwnershipPeriod>,
    val events: List<VehicleEvent>,
    val osagoContracts: List<Osago>,
    val ads: List<VehicleAd>,
    val notes: List<VehicleNote>,
    val debugInfo: DebugInfo?,

    val synchronized: Boolean = true,
) {
    val unrecognized: Boolean = brand == null
    val outdated: Boolean = currentNumber != null && number != currentNumber

    constructor(number: String, addedDate: Date = Date(Calendar.getInstance().timeInMillis)) : this(
        UUID.randomUUID().toString(),
        PlateNumber(number),
        null,
        "",
        null,
        "",
        "",
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        addedDate,
        addedDate,
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        emptyList(),
        null,
        false
    ) {

    }
}
