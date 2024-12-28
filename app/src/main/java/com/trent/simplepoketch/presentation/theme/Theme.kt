package com.trent.simplepoketch.presentation.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.MaterialTheme

// Theme data class for dynamic theming
data class Theme(
    val color1: Color,
    val color2: Color,
    val color3: Color,
    val color4: Color
)

// Define themes with corresponding colors
val themes = mapOf(
    1 to Theme(Color(0xFF70B070), Color(0xFF508050), Color(0xFF385030), Color(0xFF102818)), // Green
    2 to Theme(Color(0xFFb8b070), Color(0xFF808040), Color(0xFF485030), Color(0xFF202010)), // Yellow
    3 to Theme(Color(0xFFc09068), Color(0xFF886038), Color(0xFF504028), Color(0xFF282010)), // Orange
    4 to Theme(Color(0xFFd87070), Color(0xFFa84848), Color(0xFF602020), Color(0xFF201010)), // Red
    5 to Theme(Color(0xFFa070b0), Color(0xFF785080), Color(0xFF483050), Color(0xFF201020)), // Purple
    6 to Theme(Color(0xFF8888f8), Color(0xFF4050b8), Color(0xFF383868), Color(0xFF101820)), // Blue
    7 to Theme(Color(0xFF58b8c0), Color(0xFF208088), Color(0xFF105860), Color(0xFF201808)), // Teal
    8 to Theme(Color(0xFFa0a0a0), Color(0xFF787878), Color(0xFF505050), Color(0xFF181818))  // White
)

// Function to dynamically resolve drawable resources
fun getDrawableResource(themeIndex: Int, assetName: String, context: Context): Int {
    val themeLetter = when (themeIndex) {
        1 -> "a"
        2 -> "b"
        3 -> "c"
        4 -> "d"
        5 -> "e"
        6 -> "f"
        7 -> "g"
        8 -> "h"
        else -> "a" // Default to 'a' if index is out of range
    }

    // If assetName starts with "de", don't prepend the themeLetter
    val resourceName = if (assetName.startsWith("de")) {
        assetName
    } else {
        "${themeLetter}${assetName}"
    }

    return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
}

// Dynamic theme composable
@Composable
fun SimplePoketchTheme(
    themeIndex: Int, // Index to pick the theme
    content: @Composable () -> Unit
) {
    // Get the selected theme or fallback to default
    val selectedTheme = themes[themeIndex] ?: themes[1]!!

    MaterialTheme(
        colors = androidx.wear.compose.material.Colors(
            primary = selectedTheme.color3,    // Text color
            secondary = selectedTheme.color4, // Accent or secondary elements
            background = selectedTheme.color1, // Main background color
            onPrimary = selectedTheme.color2  // Color for text/icons on primary
        ),
        content = content
    )
}

