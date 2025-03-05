package pro.aliencat.autocat.mappers

import pro.aliencat.autocat.models.DebugInfo
import pro.aliencat.autocat.models.DebugInfoEntry
import pro.aliencat.autocat.models.Osago
import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.VehicleAd
import pro.aliencat.autocat.models.VehicleBrand
import pro.aliencat.autocat.models.VehicleEngine
import pro.aliencat.autocat.models.VehicleEvent
import pro.aliencat.autocat.models.VehicleModel
import pro.aliencat.autocat.models.VehicleNote
import pro.aliencat.autocat.models.VehicleOwnershipPeriod
import pro.aliencat.autocat.models.VehiclePhoto
import pro.aliencat.autocat.dto.DebugInfoDto
import pro.aliencat.autocat.dto.DebugInfoEntryDto
import pro.aliencat.autocat.dto.DebugInfoStatus
import pro.aliencat.autocat.dto.OsagoDto
import pro.aliencat.autocat.dto.VehicleAdDto
import pro.aliencat.autocat.dto.VehicleBrandDto
import pro.aliencat.autocat.dto.VehicleDto
import pro.aliencat.autocat.dto.VehicleEngineDto
import pro.aliencat.autocat.dto.VehicleEventDto
import pro.aliencat.autocat.dto.VehicleModelDto
import pro.aliencat.autocat.dto.VehicleNoteDto
import pro.aliencat.autocat.dto.VehicleOwnershipPeriodDto
import pro.aliencat.autocat.dto.VehiclePhotoDto
import pro.aliencat.autocat.models.common.Date

fun VehicleDto.toVehicle() = Vehicle(
    id,
    PlateNumber(number),
    currentNumber?.let { PlateNumber(it) },
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
    Date(addedDate.toLong()),
    updatedDate?.let { Date(it.toLong()) },
    photos?.map { it.toVehiclePhoto() }?.filter { it.url.isNotBlank() } ?: emptyList(),
    ownershipPeriods?.map { it.toVehicleOwnershipPeriod() } ?: emptyList(),
    events?.map { it.toVehicleEvent() } ?: emptyList(),
    osagoContracts?.map { it.toOsago() } ?: emptyList(),
    ads?.map { it.toVehicleAd() } ?: emptyList(),
    notes?.map { it.toVehicleNote() } ?: emptyList(),
    debugInfo?.toDebugInfo() ?: DebugInfoDto().toDebugInfo()
)

fun VehicleBrandDto.toVehicleBrand() = VehicleBrand(
    name?.normalized,
    name?.original,
    logo
)

fun VehicleEngineDto.toVehicleEngine(): VehicleEngine? {
    if (number?.isBlank() != false &&
        volume == null &&
        powerHp == null &&
        powerKw == null &&
        fuelType?.isBlank() != false
    )
        return null
    return VehicleEngine(number, volume, powerHp, powerKw, fuelType)
}

fun VehicleModelDto.toVehicleModel() = VehicleModel(
    name?.normalized ?: ""
)

fun VehiclePhotoDto.toVehiclePhoto(): VehiclePhoto = VehiclePhoto(url ?: "")

fun VehicleEventDto.toVehicleEvent(): VehicleEvent =
    VehicleEvent(id, Date(date.toLong()), latitude, longitude, address, addedBy)

fun VehicleEvent.toVehicleEventDto(): VehicleEventDto =
    VehicleEventDto(id, date.millisec().toDouble(), latitude, longitude, address, addedBy)

fun VehicleNoteDto.toVehicleNote(): VehicleNote = VehicleNote(id, user, Date(date.toLong()), text)

fun VehicleNote.toVehicleNoteDto(): VehicleNoteDto = VehicleNoteDto(id, user, date.millisec().toDouble(), text)

fun VehicleOwnershipPeriodDto.toVehicleOwnershipPeriod(): VehicleOwnershipPeriod =
    VehicleOwnershipPeriod(
        lastOperation,
        ownerType,
        from,
        to,
        region,
        registrationRegion,
        locality,
        code,
        street,
        building,
        inn
    )

fun VehicleAdDto.toVehicleAd(): VehicleAd =
    VehicleAd(id, url, price, Date(date.toLong()), mileage, region, city, adDescription, photos)

fun OsagoDto.toOsago(): Osago = Osago(
    Date(date.toLong()),
    number,
    vin,
    plateNumber,
    name,
    status,
    restrictions,
    insurant,
    owner,
    usageRegion,
    birthday
)

fun DebugInfoDto.toDebugInfo(): DebugInfo = DebugInfo(
    autocod.toDebugInfoEntity(),
    vin01vin.toDebugInfoEntity(),
    vin01base.toDebugInfoEntity(),
    vin01history.toDebugInfoEntity(),
    nomerogram.toDebugInfoEntity()
)

fun DebugInfoEntryDto.toDebugInfoEntity() =
    DebugInfoEntry(fields, error, DebugInfoStatus.entries.first { it.code == this.status })