package pro.aliencat.autocat.models.dto

import kotlinx.serialization.Serializable
import pro.aliencat.autocat.paging.PagingDto

@Serializable
data class VehiclePageDto(
    override val count: Int? = null,
    override val pageToken: String? = null,
    override val items: List<VehicleDto> = emptyList()
): PagingDto<VehicleDto>()
