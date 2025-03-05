package pro.aliencat.autocat.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.json.Json
import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.common.Date


@Entity
data class VehicleEntity(
    @PrimaryKey val id: String,
    val number: String,
    val currentNumber: String?,
    val vin1: String,
    val vin2: String?,
    val sts: String,
    val pts: String,
    val brand: String,
    val model: String,
    val engine: String,
    val color: String?,
    val year: Int?,
    val isRightWheel: Boolean?,
    val isJapanese: Boolean?,
    val addedBy: String?,
    val addedDate: Long,
    val updatedDate: Long?,
    val photos: String,
    val ownershipPeriods: String,
    val events: String,
    val osagoContracts: String,
    val ads: String,
    val notes: String,
    val debugInfo: String,

    val synchronized: Boolean = true,
) {
    fun toVehicle(json: Json): Vehicle = Vehicle(
        id,
        PlateNumber(number),
        currentNumber?.let { PlateNumber(it) },
        vin1,
        vin2,
        sts,
        pts,
        json.decodeFromString(brand),
        json.decodeFromString(model),
        json.decodeFromString(engine),
        color,
        year,
        isRightWheel,
        isJapanese,
        addedBy,
        Date(addedDate),
        updatedDate?.let { Date(it) },
        json.decodeFromString(photos),
        json.decodeFromString(ownershipPeriods),
        json.decodeFromString(events),
        json.decodeFromString(osagoContracts),
        json.decodeFromString(ads),
        json.decodeFromString(notes),
        json.decodeFromString(debugInfo),
    )
}

fun Vehicle.toVehicleEntity(json: Json): VehicleEntity = VehicleEntity(
    id,
    number.number,
    currentNumber?.number,
    vin1,
    vin2,
    sts,
    pts,
    json.encodeToString(brand),
    json.encodeToString(model),
    json.encodeToString(engine),
    color,
    year,
    isRightWheel,
    isJapanese,
    addedBy,
    addedDate.millisec(),
    updatedDate?.millisec(),
    json.encodeToString(photos),
    json.encodeToString(ownershipPeriods),
    json.encodeToString(events),
    json.encodeToString(osagoContracts),
    json.encodeToString(ads),
    json.encodeToString(notes),
    json.encodeToString(debugInfo),
)