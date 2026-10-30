package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HeavyLinkDarkColorScheme = darkColorScheme(
    primary = ConstructionYellow,
    onPrimary = Navy900,
    primaryContainer = Navy700,
    onPrimaryContainer = ConstructionYellowLight,
    secondary = SafetyAmber,
    onSecondary = Navy900,
    secondaryContainer = Navy600,
    onSecondaryContainer = Color.White,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    background = Navy900,
    onBackground = TextPrimary,
    surface = CardSurface,
    onSurface = TextPrimary,
    surfaceVariant = CardSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = BorderSubtle,
    error = StatusRed,
    onError = Color.White
)

private val HeavyLinkLightColorScheme = lightColorScheme(
    primary = ConstructionYellowDark,
    onPrimary = Color.White,
    primaryContainer = ConstructionYellowLight,
    onPrimaryContainer = Navy900,
    secondary = Navy800,
    onSecondary = Color.White,
    secondaryContainer = Navy500,
    onSecondaryContainer = Color.White,
    tertiary = StatusGreen,
    onTertiary = Color.White,
    background = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    surface = Color.White,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = StatusRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to Construction Dark theme for punchy industrial aesthetic
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) HeavyLinkDarkColorScheme else HeavyLinkLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
