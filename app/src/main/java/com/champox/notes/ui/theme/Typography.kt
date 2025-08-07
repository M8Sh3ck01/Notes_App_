// Typography.kt
package com.champox.notes.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

// This is your custom typography for Material 3 components
val NotesTypography = Typography(
    // Main body text style
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,  // You can replace this with your custom font family
        fontSize = 16.sp                  // Default body text size
    ),
    // Headline used for titles or major sections
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 24.sp
    ),
    // Optional small label (like button or caption)
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 12.sp
    )
)
