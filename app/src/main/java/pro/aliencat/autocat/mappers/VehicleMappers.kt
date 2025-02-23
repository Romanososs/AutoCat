package pro.aliencat.autocat.mappers

import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.VehicleBrand
import pro.aliencat.autocat.models.VehicleEngine
import pro.aliencat.autocat.models.VehicleModel
import pro.aliencat.autocat.models.common.Date
import pro.aliencat.autocat.models.dto.VehicleBrandDto
import pro.aliencat.autocat.models.dto.VehicleDto
import pro.aliencat.autocat.models.dto.VehicleEngineDto
import pro.aliencat.autocat.models.dto.VehicleModelDto
import pro.aliencat.autocat.models.dto.VehiclePageDto
import pro.aliencat.autocat.ui.screens.search.model.SearchResultSection

fun VehicleDto.toVehicle() = Vehicle(
    id,
    PlateNumber(number),
    currentNumber?.let {PlateNumber(it)},
    vin1 ?: "",
    vin2 ?: "",
    sts ?: "",
    pts ?: "",
    brand?.toVehicleBrand(),
    model?.toVehicleModel(),
    engine?.toVehicleEngine(),
    color,
    year,
    isRightWheel,
    isJapanese,
    addedBy,
    Date(addedDate),
    updatedDate?.let { Date(it) }
)

fun VehicleBrandDto.toVehicleBrand() = VehicleBrand(
    name?.normalized ?: "",
    name?.original ?: "",
    logo
)

fun VehicleEngineDto.toVehicleEngine(): VehicleEngine? {
    if (number?.isBlank() != false &&
        volume == null &&
        powerHp == null &&
        powerKw == null &&
        fuelType?.isBlank() != false)
        return null
    return VehicleEngine(
        number,
        volume,
        powerHp,
        powerKw,
        fuelType
    )
}

fun VehicleModelDto.toVehicleModel() = VehicleModel(
    name?.normalized ?: ""
)