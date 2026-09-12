package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ApexColorScheme = darkColorScheme(
    primary = ApexPurple,
    onPrimary = Color.White,
    primaryContainer = ApexPurpleDark,
    onPrimaryContainer = Color.White,
    secondary = ApexBlue,
    onSecondary = Color.White,
    secondaryContainer = ApexButtonSurface,
    onSecondaryContainer = ApexTextPrimary,
    tertiary = ApexIndigo,
    onTertiary = Color.White,
    background = ApexDarkBackground,
    onBackground = ApexTextPrimary,
    surface = ApexDarkSurface,
    onSurface = ApexTextPrimary,
    surfaceVariant = ApexDarkSurfaceVariant,
    onSurfaceVariant = ApexTextSecondary,
    outline = ApexBorder,
    outlineVariant = ApexBorderGlow
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = ApexColorScheme,
        typography = Typography,
        content = content
    )
}
