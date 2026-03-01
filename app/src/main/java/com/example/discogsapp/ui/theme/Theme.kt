package com.example.discogsapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
    darkColorScheme(
        primary = DiscogsBlueLight,
        onPrimary = BackgroundDark,
        primaryContainer = DiscogsBlueDark,
        onPrimaryContainer = DiscogsBlueLight,
        secondary = AccentOrangeLight,
        onSecondary = BackgroundDark,
        background = BackgroundDark,
        onBackground = ColorWhite,
        surface = SurfaceDark,
        onSurface = ColorWhite,
    )

private val LightColorScheme =
    lightColorScheme(
        primary = DiscogsBlue,
        onPrimary = ColorWhite,
        primaryContainer = DiscogsBlueLight,
        onPrimaryContainer = DiscogsBlueDark,
        secondary = AccentOrange,
        onSecondary = ColorWhite,
        background = BackgroundLight,
        onBackground = ColorBlack,
        surface = ColorWhite,
        onSurface = ColorBlack,
    )

@Composable
fun DiscogsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            }

            darkTheme -> {
                DarkColorScheme
            }

            else -> {
                LightColorScheme
            }
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
