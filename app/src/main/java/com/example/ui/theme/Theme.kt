package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = AmarOrangeLight,
    onPrimary = Color(0xFF370B00),
    primaryContainer = AmarOrangeDark,
    onPrimaryContainer = Color(0xFFFFDBCF),
    secondary = AmarTealLight,
    onSecondary = Color(0xFF003731),
    secondaryContainer = AmarTealDark,
    onSecondaryContainer = Color(0xFFB2DFDB),
    tertiary = AmarGoldAccent,
    background = AmarBackgroundDark,
    onBackground = AmarTextPrimaryDark,
    surface = AmarSurfaceDark,
    onSurface = AmarTextPrimaryDark,
    surfaceVariant = AmarSurfaceVariantDark,
    onSurfaceVariant = AmarTextSecondaryDark,
    outline = AmarBorderDark,
    error = AmarErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = AmarOrangePrimary,
    onPrimary = Color.White,
    primaryContainer = AmarOrangeContainer,
    onPrimaryContainer = AmarOrangeOnContainer,
    secondary = AmarTealSecondary,
    onSecondary = Color.White,
    secondaryContainer = AmarTealContainer,
    onSecondaryContainer = AmarTealOnContainer,
    tertiary = AmarGoldAccent,
    background = AmarBackgroundLight,
    onBackground = AmarTextPrimaryLight,
    surface = AmarSurfaceLight,
    onSurface = AmarTextPrimaryLight,
    surfaceVariant = AmarSurfaceVariantLight,
    onSurfaceVariant = AmarTextSecondaryLight,
    outline = AmarBorderLight,
    error = AmarErrorRed
)

@Composable
fun AmarShopTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set false by default to showcase AmarShop vibrant orange & teal brand identity
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AmarShopTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}
