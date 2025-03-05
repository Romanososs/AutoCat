package pro.aliencat.autocat.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VehicleDto(
    @SerialName("_id")
    val id: String,
    val number: String = "",
    val currentNumber: String? = null,
    val vin1: String? = null,
    val vin2: String? = null,
    val sts: String? = null,
    val pts: String? = null,
    val brand: VehicleBrandDto? = null,
    val model: VehicleModelDto? = null,
    val engine: VehicleEngineDto? = null,
    val color: String? = null,
    val year: Int? = null,
    val isRightWheel: Boolean? = null,
    val isJapanese: Boolean? = null,
    val addedBy: String? = null,
    val addedDate: Double = 0.0,
    val updatedDate: Double? = null,
    val photos: List<VehiclePhotoDto>? = null,
    val ownershipPeriods: List<VehicleOwnershipPeriodDto>? = null,
    val events: List<VehicleEventDto>? = null,
    val osagoContracts: List<OsagoDto>? = null,
    val ads: List<VehicleAdDto>? = null,
    val notes: List<VehicleNoteDto>? = null,
    val debugInfo: DebugInfoDto? = null,
)
