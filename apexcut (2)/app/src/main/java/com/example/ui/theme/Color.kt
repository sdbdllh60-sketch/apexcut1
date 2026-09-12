package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Sophisticated Dark Theme Base Colors
val ApexDarkBackground = Color(0xFF0A0A0C)
val ApexDarkSurface = Color(0xFF16161A)
val ApexDarkSurfaceVariant = Color(0xFF1E1E24)
val ApexDarkCard = Color(0xFF16161A)
val ApexDarkCardElevated = Color(0xFF1E1E24)
val ApexDarkNav = Color(0xFF0F0F12)
val ApexBorder = Color(0xFF2A2A30)
val ApexBorderGlow = Color(0xFF3F3F4A)
val ApexButtonSurface = Color(0xFF1E1E22)

// Sophisticated Accents
val ApexPurple = Color(0xFF8B5CF6)
val ApexPurpleDark = Color(0xFF7C3AED)
val ApexPurpleLight = Color(0xFFA78BFA)
val ApexPurpleBg = Color(0xFF2D1B4E)

val ApexIndigo = Color(0xFF6366F1)

val ApexBlue = Color(0xFF3B82F6)
val ApexBlueDark = Color(0xFF1D4ED8)
val ApexBlueLight = Color(0xFF60A5FA)
val ApexBlueBg = Color(0xFF1B2A4E)

val ApexCyan = Color(0xFF06B6D4)
val ApexCyanLight = Color(0xFF67E8F9)
val ApexLogoCyan = Color(0xFF00E5FF)
val ApexLogoBlue = Color(0xFF0091EA)
val ApexLogoRed = Color(0xFFFF1744)
val ApexCrimson = Color(0xFFEF4444)
val ApexCrimsonPink = Color(0xFFF43F5E)

val ApexPink = Color(0xFFEC4899)
val ApexOrange = Color(0xFFF97316)
val ApexAmber = Color(0xFFF59E0B)
val ApexEmerald = Color(0xFF10B981)
val ApexYellow = Color(0xFFFBBF24)

val ApexTextPrimary = Color(0xFFFFFFFF)
val ApexTextSecondary = Color(0xFF9CA3AF)
val ApexTextMuted = Color(0xFF6B7280)

// Gradients
val ApexLogoGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF00B0FF), Color(0xFFFF2A6D))
)

val ApexPremiumGradient = Brush.horizontalGradient(
    colors = listOf(Color(0xFF8B5CF6), Color(0xFFEC4899), Color(0xFFEF4444))
)

val ApexHeroGradient = Brush.horizontalGradient(
    colors = listOf(ApexPurpleDark, ApexIndigo, ApexBlue)
)

val ApexPrimaryGradient = Brush.horizontalGradient(
    colors = listOf(ApexPurple, ApexIndigo, ApexBlue)
)

val ApexCyanBlueGradient = Brush.horizontalGradient(
    colors = listOf(ApexCyan, ApexBlue)
)

val ApexNeonGlowGradient = Brush.linearGradient(
    colors = listOf(
        ApexPurple.copy(alpha = 0.35f),
        ApexBlue.copy(alpha = 0.35f),
        ApexCyan.copy(alpha = 0.15f)
    )
)

val ApexCardGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF1B1B20),
        Color(0xFF16161A)
    )
)

val ApexGoldGradient = Brush.horizontalGradient(
    colors = listOf(ApexAmber, ApexYellow)
)

