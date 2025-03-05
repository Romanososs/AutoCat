package pro.aliencat.autocat

import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.VehicleBrand
import pro.aliencat.autocat.models.VehicleEngine
import pro.aliencat.autocat.models.VehicleModel

val vehicleDummy = Vehicle(
    number = PlateNumber("A456AA78"),
    currentNumber = null,
    vin1 = "",
    vin2 = "",
    sts = "",
    pts = "",
    brand = VehicleBrand("KIA", null),
    model = VehicleModel("OPTIMA"),
    engine = VehicleEngine(null,null, null,null,null),
    color = "",
    year = 2000,
    isRightWheel = true,
    isJapanese = false,
    addedBy = "test@test.com"
)