package com.atech.ui_common.theme

import android.app.Activity
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val lightColorScheme = ColorScheme(
    background = Color(0xfff8f9fb),
    error = Color(0xffba1a1a),
    errorContainer = Color(0xffffdad6),
    inverseOnSurface = Color(0xfff0f1f3),
    inversePrimary = Color(0xff99cdeb),
    inverseSurface = Color(0xff2e3133),
    onBackground = Color(0xff191c1e),
    onError = Color(0xffffffff),
    onErrorContainer = Color(0xff410002),
    onPrimary = Color(0xffffffff),
    onPrimaryContainer = Color(0xff000910),
    onSecondary = Color(0xffffffff),
    onSecondaryContainer = Color(0xff384a55),
    onSurface = Color(0xff191c1e),
    onSurfaceVariant = Color(0xff41484c),
    onTertiary = Color(0xffffffff),
    onTertiaryContainer = Color(0xff100500),
    outline = Color(0xff71787d),
    outlineVariant = Color(0xffc1c9cf),
    primary = Color(0xff2e647e),
    primaryContainer = Color(0xff6ca0bc),
    scrim = Color(0xff000000),
    secondary = Color(0xff4e616c),
    secondaryContainer = Color(0xffd1e5f3),
    surface = Color(0xfff8f9fb),
    surfaceTint = Color(0xff2e647e),
    surfaceVariant = Color(0xffdce3e9),
    tertiary = Color(0xff825425),
    tertiaryContainer = Color(0xffc58e59),
    surfaceBright = Color(0xfff8f9fb),
    surfaceDim = Color(0xffd9dadc),
    surfaceContainer = Color(0xffedeef0),
    surfaceContainerHigh = Color(0xffe7e8ea),
    surfaceContainerHighest = Color(0xffe1e2e5),
    surfaceContainerLow = Color(0xfff3f3f6),
    surfaceContainerLowest = Color(0xffffffff),
)

val darkColorScheme = ColorScheme(
    background = Color(0xff111415),
    error = Color(0xffffb4ab),
    errorContainer = Color(0xff93000a),
    inverseOnSurface = Color(0xff2e3133),
    inversePrimary = Color(0xff2e647e),
    inverseSurface = Color(0xffe1e2e5),
    onBackground = Color(0xffe1e2e5),
    onError = Color(0xff690005),
    onErrorContainer = Color(0xffffdad6),
    onPrimary = Color(0xff003548),
    onPrimaryContainer = Color(0xff000910),
    onSecondary = Color(0xff20333d),
    onSecondaryContainer = Color(0xffd3e7f4),
    onSurface = Color(0xffe1e2e5),
    onSurfaceVariant = Color(0xffc0c7cd),
    onTertiary = Color(0xff4a2800),
    onTertiaryContainer = Color(0xff100500),
    outline = Color(0xff8a9297),
    outlineVariant = Color(0xff434e54),
    primary = Color(0xff99cdeb),
    primaryContainer = Color(0xff6ca0bc),
    scrim = Color(0xff000000),
    secondary = Color(0xffb6c9d6),
    secondaryContainer = Color(0xff394c56),
    surface = Color(0xff111415),
    surfaceTint = Color(0xff99cdeb),
    surfaceVariant = Color(0xff41484c),
    tertiary = Color(0xfff7ba81),
    tertiaryContainer = Color(0xffc58e59),
    surfaceBright = Color(0xff37393b),
    surfaceDim = Color(0xff111415),
    surfaceContainer = Color(0xff1d2022),
    surfaceContainerHigh = Color(0xff282a2c),
    surfaceContainerHighest = Color(0xff333537),
    surfaceContainerLow = Color(0xff191c1e),
    surfaceContainerLowest = Color(0xff0c0f10),
)
@Immutable
data class SystemTheme(
    val colorScheme: ColorScheme,
    val navBarColor: Color = colorScheme.surface,
)

val LocalTheme = compositionLocalOf { SystemTheme(lightColorScheme) }

@Composable
fun ResearchHubTheme(
    systemTheme: SystemTheme = SystemTheme(
        colorScheme = lightColorScheme
    ),
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalTheme provides systemTheme,
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = LocalTheme.current.colorScheme,
            typography = Typography,
            content = content
        )
        val view = LocalView.current
        val color = LocalTheme.current.navBarColor.toArgb()
        if (!view.isInEditMode) {
            SideEffect {
                val window = (view.context as Activity).window
                window.navigationBarColor = color
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars =
                    true
            }
        }
    }
}