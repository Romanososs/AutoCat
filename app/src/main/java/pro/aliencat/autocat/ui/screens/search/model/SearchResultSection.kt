package pro.aliencat.autocat.ui.screens.search.model

import pro.aliencat.autocat.models.Vehicle

data class SearchResultSection(
    val title: String = "",
    val elements: List<Vehicle> = listOf()
) {
//    var dateString: String = ""
//        private set
//
//    fun getSize() = elements.size
//
//    fun add(vehicle: Vehicle): Boolean {
//        val vehicleDate = vehicle.updatedDate ?: vehicle.addedDate
//        if (elements.isEmpty()) {
//            date = vehicleDate
//            dateString = date.toHeaderString()
//            elements.add(vehicle)
//            return true
//        }
//        if (vehicleDate.toHeaderString() == dateString) {
//            elements.add(vehicle)
//            return true
//        }
//        return false
//    }
}