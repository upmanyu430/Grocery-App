package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val FrostedGlassColorScheme = lightColorScheme(
    primary = Emerald600,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    secondary = Emerald700,
    background = BackgroundColor,
    surface = GlassWhite40,
    onBackground = Slate900,
    onSurface = Slate800,
    surfaceVariant = GlassWhite60,
    onSurfaceVariant = Slate500,
    secondaryContainer = Emerald50,
    onSecondaryContainer = Emerald600,
    error = androidx.compose.ui.graphics.Color(0xFFEF4444),
    onError = androidx.compose.ui.graphics.Color.White,
    outline = BorderWhite40,
    surfaceTint = androidx.compose.ui.graphics.Color.Transparent
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = FrostedGlassColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
