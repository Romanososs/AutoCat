package pro.aliencat.autocat.ui.theme

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat


@Immutable
data class CustomColorsPalette(
    val foregroundPlateColor: Color = Color.Unspecified,
    val backgroundPlateColor: Color = Color.Unspecified,
    val disabledPlateColor: Color = Color.Unspecified,
    val mainElement: Color = Color.Unspecified,
    val subElement: Color = Color.Unspecified,
)

@SuppressLint("CompositionLocalNaming")
val CustomTheme = staticCompositionLocalOf { CustomColorsPalette() }

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    outline = Color.DarkGray
)

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    outline = Color.LightGray
)


private val LightCustomColorScheme = CustomColorsPalette(
    foregroundPlateColor = Color.Black,
    backgroundPlateColor = Color.White,
    disabledPlateColor = DisablePlateGreyLight,
    mainElement = MainDateLight,
    subElement = SubDateLight
)

private val DarkCustomColorScheme = CustomColorsPalette(
    foregroundPlateColor = Color(0xFFDCDCDC),
    backgroundPlateColor = Color(0xFF262626),
    disabledPlateColor = DisablePlateGreyDark,
    mainElement = MainDateDark,
    subElement = SubDateDark
)


@Suppress("DEPRECATION")
@Composable
    fun AutoCatTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }
    val customColorsPalette =
        if (darkTheme) DarkCustomColorScheme
        else LightCustomColorScheme

    CompositionLocalProvider(
        CustomTheme provides customColorsPalette,

    ) {
        CompositionLocalProvider(
            CustomTypography provides CustomTypeScheme
        ) {
            MaterialTheme(
                colorScheme = colorScheme,
                typography = Typography,
                content = content
            )
        }
    }
}