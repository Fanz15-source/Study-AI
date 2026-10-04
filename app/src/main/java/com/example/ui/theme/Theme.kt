package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// 1. Dark Focus (Default Night Study)
val DarkFocusColorScheme = darkColorScheme(
    primary = PrimaryDark,
    onPrimary = DeepNavy,
    primaryContainer = MidnightBlue,
    onPrimaryContainer = SoftLavender,
    secondary = SecondaryDark,
    onSecondary = DeepNavy,
    secondaryContainer = SurfaceElevatedDark,
    onSecondaryContainer = ElectricBlue,
    tertiary = TertiaryDark,
    onTertiary = DeepNavy,
    background = DeepNavy,
    onBackground = TextPrimaryDark,
    surface = MidnightBlue,
    onSurface = TextPrimaryDark,
    surfaceVariant = SurfaceElevatedDark,
    onSurfaceVariant = TextSecondaryDark,
    outline = SurfaceBorderDark,
    outlineVariant = Color(0xFF3B487A),
    error = CrimsonError,
    onError = SoftWhite
)

// 2. Light Calm
val LightCalmColorScheme = lightColorScheme(
    primary = PrimaryLight,
    onPrimary = SoftWhite,
    primaryContainer = Color(0xFFE9E4FF),
    onPrimaryContainer = PrimaryLight,
    secondary = SecondaryLight,
    onSecondary = SoftWhite,
    secondaryContainer = Color(0xFFE3EDFF),
    onSecondaryContainer = SecondaryLight,
    tertiary = TertiaryLight,
    onTertiary = SoftWhite,
    background = WarmCream,
    onBackground = TextPrimaryLight,
    surface = SurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = SurfaceContainerLight,
    onSurfaceVariant = TextSecondaryLight,
    outline = SurfaceBorderLight,
    outlineVariant = Color(0xFFD5CFC3),
    error = CrimsonError,
    onError = SoftWhite
)

// 3. Cyber Indigo
val CyberIndigoColorScheme = darkColorScheme(
    primary = CyberPrimary,
    onPrimary = Color(0xFF031628),
    primaryContainer = Color(0xFF0C2440),
    onPrimaryContainer = CyberPrimary,
    secondary = CyberSecondary,
    onSecondary = Color(0xFF0B0F19),
    secondaryContainer = Color(0xFF1E293B),
    onSecondaryContainer = CyberSecondary,
    tertiary = CyberTertiary,
    onTertiary = Color(0xFF042017),
    background = CyberBg,
    onBackground = SoftWhite,
    surface = CyberSurface,
    onSurface = SoftWhite,
    surfaceVariant = Color(0xFF1E2945),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF2A3A60),
    error = CrimsonError,
    onError = SoftWhite
)

// 4. Forest Matcha
val ForestMatchaColorScheme = darkColorScheme(
    primary = ForestPrimary,
    onPrimary = Color(0xFF04241C),
    primaryContainer = Color(0xFF0F362C),
    onPrimaryContainer = ForestPrimary,
    secondary = ForestSecondary,
    onSecondary = Color(0xFF04241C),
    secondaryContainer = Color(0xFF194237),
    onSecondaryContainer = ForestSecondary,
    tertiary = ForestTertiary,
    onTertiary = Color(0xFF04241C),
    background = ForestBg,
    onBackground = SoftWhite,
    surface = ForestSurface,
    onSurface = SoftWhite,
    surfaceVariant = Color(0xFF1B3831),
    onSurfaceVariant = Color(0xFFA7C2B9),
    outline = Color(0xFF254D43),
    error = CrimsonError,
    onError = SoftWhite
)

// 5. Twilight Amber
val TwilightAmberColorScheme = darkColorScheme(
    primary = TwilightPrimary,
    onPrimary = Color(0xFF29081B),
    primaryContainer = Color(0xFF3D162D),
    onPrimaryContainer = TwilightPrimary,
    secondary = TwilightSecondary,
    onSecondary = Color(0xFF2E1A03),
    secondaryContainer = Color(0xFF4A3210),
    onSecondaryContainer = TwilightSecondary,
    tertiary = TwilightTertiary,
    onTertiary = Color(0xFF29081B),
    background = TwilightBg,
    onBackground = SoftWhite,
    surface = TwilightSurface,
    onSurface = SoftWhite,
    surfaceVariant = Color(0xFF37264D),
    onSurfaceVariant = Color(0xFFC7B3DC),
    outline = Color(0xFF4A3568),
    error = CrimsonError,
    onError = SoftWhite
)

@Composable
fun AIStudyHubTheme(
    themeKey: String = "DARK_FOCUS",
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeKey) {
        "LIGHT_CALM" -> LightCalmColorScheme
        "CYBER_INDIGO" -> CyberIndigoColorScheme
        "FOREST_MATCHA" -> ForestMatchaColorScheme
        "TWILIGHT_AMBER" -> TwilightAmberColorScheme
        else -> DarkFocusColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AIStudyHubTheme(themeKey = if (darkTheme) "DARK_FOCUS" else "LIGHT_CALM", content = content)
}
