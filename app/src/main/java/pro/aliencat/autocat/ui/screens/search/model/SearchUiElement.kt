package pro.aliencat.autocat.ui.screens.search.model

import pro.aliencat.autocat.models.Vehicle

sealed class SearchUiElement{
    //щас бы var-ы юзать(
    data class StickyHeader(val dateString: String, var count: Int = 1): SearchUiElement()
    data class VehicleObj(val vehicle: Vehicle, var isLastInSection: Boolean = false): SearchUiElement()
}