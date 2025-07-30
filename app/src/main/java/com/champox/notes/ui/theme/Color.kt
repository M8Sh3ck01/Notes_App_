//Color.kt file below


package com.champox.notes.ui.theme

import androidx.compose.ui.graphics.Color

// --- Monochrome Base Colors ---
val PureWhite   = Color(0xFFFFFFFF)
val PureBlack   = Color(0xFF000000)
val GrayLight   = Color(0xFFF5F5F5)
val GrayMedium  = Color(0xFF9E9E9E)
val GrayDark    = Color(0xFF212121)

// --- Surface Variants (after gray values) ---
val SurfaceVariantLight   = GrayLight
val OnSurfaceVariantLight = PureBlack.copy(alpha = 0.8f)

val SurfaceVariantDark    = GrayDark
val OnSurfaceVariantDark  = PureWhite.copy(alpha = 0.85f)

// --- Accent and Branding Colors ---
val PrimaryColor     = Color(0xFF3949AB)  // Indigo 600
val SecondaryColor   = Color(0xFFFFC107)  // Amber 500

// --- Light Theme ---
val LightBackground   = Color(0xFFFDFDFD)
val LightSurface      = Color(0xFFFFFFFF)
val LightOnPrimary    = Color.White
val LightOnBackground = Color(0xFF121212)
val LightOnSurface    = Color(0xFF121212)
val LightOnSecondary  = Color.Black

// --- Dark Theme ---
val DarkBackground    = Color(0xFF131520)
val CardBackground    = Color(0xFF282D43)  // Used as "surface"
val PrimaryText       = Color(0xFFFFFFFF)
val SecondaryText     = Color(0xFF99A0C2)
val AccentColor       = Color(0xFFEAEDFA)
val IconBackground    = Color(0xFF282D43)

