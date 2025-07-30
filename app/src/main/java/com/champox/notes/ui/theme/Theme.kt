
//Theme.kt file
package com.champox.notes.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary            = PureBlack,
    onPrimary          = PureWhite,
    secondary          = GrayMedium,
    onSecondary        = PureWhite,
    background         = PureWhite,
    onBackground       = PureBlack,
    surface            = GrayLight,
    onSurface          = PureBlack,
    surfaceVariant     = SurfaceVariantLight,
    onSurfaceVariant   = OnSurfaceVariantLight,
)

private val DarkColors = darkColorScheme(
    primary            = PureWhite,
    onPrimary          = PureBlack,
    secondary          = GrayMedium,
    onSecondary        = PureBlack,
    background         = PureBlack,
    onBackground       = PureWhite,
    surface            = GrayDark,
    onSurface          = PureWhite,
    surfaceVariant     = SurfaceVariantDark,
    onSurfaceVariant   = OnSurfaceVariantDark,
)


@Composable
fun NotesTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = MaterialTheme.typography,
        content     = content
    )
}

