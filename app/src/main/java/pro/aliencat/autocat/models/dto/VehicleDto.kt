package pro.aliencat.autocat.models.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.Instant

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
    val addedDate: Long = 0,
    val updatedDate: Long? = null,

//            public var addedDate: TimeInterval = 0
//            public var updatedDate: TimeInterval = 0
//            public var photos: [VehiclePhotoDto] = []
//            public var ownershipPeriods: [VehicleOwnershipPeriodDto] = []
//            public var events: [VehicleEventDto] = []
//            public var osagoContracts: [OsagoDto] = []
//            public var ads: [VehicleAdDto] = []
//            public var notes: [VehicleNoteDto] = []
//            public var debugInfo: DebugInfoDto?
)
