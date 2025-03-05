package pro.aliencat.autocat.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import pro.aliencat.autocat.models.PlateNumber
import pro.aliencat.autocat.ui.theme.AutoCatTheme
import pro.aliencat.autocat.ui.theme.BrightRed
import pro.aliencat.autocat.ui.theme.CustomTheme
import pro.aliencat.autocat.ui.theme.CustomTypography

@Composable
fun PlateNumberView(number: PlateNumber, foregroundColor: Color, modifier: Modifier = Modifier) {

    val numberPartFraction = 0.7F
    val border = 2.dp
    val cornerRadius = 5.dp
    val innerPadding = 2.dp

    val aspectRatio = 520F/112F
    val plateWidth = 180.dp
    val flagSize = DpSize(16.dp, 10.dp)

    Surface(
        shape = RoundedCornerShape(cornerRadius),
        color = foregroundColor,
        modifier = modifier.widthIn(max = plateWidth).aspectRatio(aspectRatio)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(border)
        ) {
            Surface(
                shape = RoundedCornerShape(cornerRadius),
                color = CustomTheme.current.backgroundPlateColor,
                contentColor = foregroundColor,
                modifier = Modifier
                    .fillMaxWidth(numberPartFraction)
                    .fillMaxHeight()
                    .padding(end = border)
            ) {
                Text(
                    number.mainPart,
                    style = CustomTypography.current.numberPlateLarge,
                    modifier = Modifier.padding(innerPadding)
                )
            }
            Surface(
                shape = RoundedCornerShape(cornerRadius),
                color = CustomTheme.current.backgroundPlateColor,
                contentColor = foregroundColor,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Text(number.region, style = CustomTypography.current.numberPlateMedium)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text("RUS", style = CustomTypography.current.numberPlateSmall)//TODO
                        Column(modifier = Modifier.size(flagSize).border(BorderStroke(0.4.dp, Color.Black))) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1F)
                                    .background(Color.White)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1F)
                                    .background(Color.Blue)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1F)
                                    .background(Color.Red)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PlateViewPreview1() {
    AutoCatTheme {
        PlateNumberView(
            PlateNumber("О555МН161"),
            CustomTheme.current.foregroundPlateColor,
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlateViewPreview2() {
    AutoCatTheme {
        PlateNumberView(
            PlateNumber("О555ЖН72"),
            CustomTheme.current.foregroundPlateColor,
        )
    }
}

@Preview
@Composable
fun PlateViewPreview3() {
    AutoCatTheme {
        PlateNumberView(
            PlateNumber("О555ЖН761"),
            BrightRed,
        )
    }
}

@Preview
@Composable
fun PlateViewPreview4() {
    AutoCatTheme {
        PlateNumberView(
            PlateNumber("О555ЖН72"),
            CustomTheme.current.disabledPlateColor,
        )
    }
}
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PlateViewPreview5() {
    AutoCatTheme {
        PlateNumberView(
            PlateNumber("О555ЖН72"),
            CustomTheme.current.disabledPlateColor,
        )
    }
}