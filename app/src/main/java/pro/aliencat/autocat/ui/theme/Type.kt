package pro.aliencat.autocat.ui.theme

import android.annotation.SuppressLint
import androidx.compose.material3.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import pro.aliencat.autocat.R

val plateFont = FontFamily(
    Font(R.font.plate_font, FontWeight.Normal),
)

@Immutable
data class CustomTypePalette(
    val numberPlateLarge: TextStyle = TextStyle.Default,
    val numberPlateMedium: TextStyle = TextStyle.Default,
    val numberPlateSmall: TextStyle = TextStyle.Default,
)

@SuppressLint("CompositionLocalNaming")
val CustomTypography = staticCompositionLocalOf { CustomTypePalette() }

val CustomTypeScheme = CustomTypePalette(
    numberPlateLarge = TextStyle(
        fontFamily = plateFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        baselineShift = BaselineShift(-0.4F),
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center
    ),
    numberPlateMedium = TextStyle(
        fontFamily = plateFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        letterSpacing = 1.sp,
        baselineShift = BaselineShift.Subscript,
        textAlign = TextAlign.Center
    ),
    numberPlateSmall = TextStyle(
        fontFamily = plateFont,
        fontWeight = FontWeight.Light,
        fontSize = 10.sp,
        baselineShift = BaselineShift(-0.1F),
        textAlign = TextAlign.Center
    ),
)

// Set of Material typography styles to start with
val Typography = Typography(

)
