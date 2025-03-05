package pro.aliencat.autocat.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pro.aliencat.autocat.R
import pro.aliencat.autocat.mappers.toDebugInfo
import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.models.Vehicle
import pro.aliencat.autocat.models.VehicleBrand
import pro.aliencat.autocat.models.VehicleEngine
import pro.aliencat.autocat.models.VehicleModel
import pro.aliencat.autocat.models.common.Date
import pro.aliencat.autocat.dto.DebugInfoDto
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.BrightRed
import pro.aliencat.autocat.ui.theme.CustomTheme
import pro.aliencat.autocat.ui.theme.LightBlue
import pro.aliencat.autocat.ui.theme.Orange
import java.util.UUID


@Composable
fun VehicleCellView(vehicle: Vehicle) {
    val showUpdateDate = remember(vehicle.updatedDate) {
        ((vehicle.updatedDate ?: Date.Empty) - vehicle.addedDate).millisec() > 10 * 1000
    }
    val plateColor = if (vehicle.unrecognized) {
        BrightRed
    } else if (vehicle.outdated) {
        CustomTheme.current.disabledPlateColor
    } else {
        CustomTheme.current.foregroundPlateColor
    }
    Surface {//FIXME мб поменять цвета, чтобы строки отличались от топбара
        Column {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp)
                ) {
                    vehicle.brand?.fullName?.let {
                        Text(it, style = MaterialTheme.typography.titleMedium)
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!vehicle.synchronized && !vehicle.unrecognized) {
                            Icon(
                                painterResource(R.drawable.sync_problem_24dp),
                                null, //TODO
                                tint = Orange
                            )
                        }
                        if (vehicle.notes.isNotEmpty()) {
                            Icon(
                                painterResource(R.drawable.chat_24dp),
                                null,//TODO
                                modifier = Modifier.size(16.dp),
                                tint = LightBlue
                            )
                            Text(vehicle.notes.size.toString())
                        }
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PlateNumberView(vehicle.number, plateColor)
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (showUpdateDate) {
                            Text(
                                vehicle.updatedDate.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = CustomTheme.current.mainElement,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                        Text(
                            vehicle.addedDate.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (showUpdateDate) CustomTheme.current.subElement
                            else CustomTheme.current.mainElement
                        )
                    }
                }
            }
            HorizontalDivider(thickness = 0.3.dp)
        }
    }
}

@Preview
@Composable
fun VehicleCellViewPreview1() {
    AutoCatTheme {
        VehicleCellView(vehicleDummy)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun VehicleCellViewPreview2() {
    AutoCatTheme {
        VehicleCellView(vehicleDummy)
    }
}

//TODO delete!
val vehicleDummy = Vehicle(
    UUID.randomUUID().toString(),
    number = PlateNumber("A456AA78"),
    currentNumber = null,
    vin1 = "",
    vin2 = "",
    sts = "",
    pts = "",
    brand = VehicleBrand("KIA", "KIA OPTIMA", null),
    model = VehicleModel("OPTIMA"),
    engine = VehicleEngine(null, null, null, null, null),
    color = "",
    year = 2000,
    isRightWheel = true,
    isJapanese = false,
    addedBy = "test@test.com",
    addedDate = Date(1617477720504),
    updatedDate = Date(1617577720504),
    emptyList(),
    emptyList(),
    emptyList(),
    emptyList(),
    emptyList(),
    emptyList(),
    DebugInfoDto().toDebugInfo()
)

