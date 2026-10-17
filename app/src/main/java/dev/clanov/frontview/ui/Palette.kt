package dev.clanov.frontview.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

object Palette {
    val Background = Color.White
    val Tile = Color(0xFFECECF1)
    val TileDeep = Color(0xFFE1E1E8)
    val Grouped = Color(0xFFF2F2F7)
    val Ink = Color(0xFF1D1D1F)
    val Muted = Color(0xFF6E6E73)
    val Faint = Color(0xFF8E8E93)
    val Divider = Color(0xFFD9D9E0)
    val Hairline = Color(0xFFE5E5EA)
    val Ring = Color(0xFFB0B0B8)
    val Placeholder = Color(0xFFF6F6F9)
    val PlaceholderBorder = Color(0xFFC7C7CF)

    val Lavender = Color(0xFF7468C9)
    val LavenderLight = Color(0xFFE4E1F4)
    val LavenderDark = Color(0xFF4D42A6)

    val Red = Color(0xFFD6353A)
    val MailRed = Color(0xFFD62839)
    val Blue = Color(0xFF1473D6)
    val BlueLight = Color(0xFFE6F0FB)
    val Event = Color(0xFF3B82F6)
    val Green = Color(0xFF22A45D)
    val GreenDark = Color(0xFF178A4A)
    val Purple = Color(0xFF8A5CC6)
    val Orange = Color(0xFFF08A24)
    val Yellow = Color(0xFFFFD45C)
    val Live = Color(0xFFFF5A5F)

    val GeminiBlue = Color(0xFF4285F4)
    val GeminiInk = Color(0xFF1A56C4)
    val GeminiViolet = Color(0xFF9B72CB)

    val OnDark90 = Color.White.copy(alpha = 0.9f)
    val OnDark85 = Color.White.copy(alpha = 0.85f)
    val OnDark70 = Color.White.copy(alpha = 0.7f)
    val OnDark60 = Color.White.copy(alpha = 0.6f)
    val Glass = Color.White.copy(alpha = 0.16f)
    val GlassSoft = Color.White.copy(alpha = 0.1f)
    val GlassStrong = Color.White.copy(alpha = 0.22f)

    private fun diagonal(vararg colors: Long): Brush =
        Brush.linearGradient(colors.map { Color(it) })

    val Weather: Brush = diagonal(0xFF2F7BE0, 0xFF1F5CC0)
    val Navy: Brush = diagonal(0xFF1B2A52, 0xFF0E1630)
    val MusicDark: Brush = diagonal(0xFF1B2240, 0xFF0E1226)
    val Art: Brush = diagonal(0xFFFA2D48, 0xFFA23CFF, 0xFF2F6BFF)
    val Insta: Brush = diagonal(0xFFC2308F, 0xFF6A35D6)
    val Gemini: Brush = diagonal(0xFFE8F0FE, 0xFFE0D9F6)
    val NewsFeature: Brush = diagonal(0xFF3F7DE0, 0xFF1F4FA8)

    fun orb(width: Float): Brush = Brush.radialGradient(
        0f to Color.White,
        0.35f to Color(0xFFA8C7FA),
        0.7f to GeminiBlue,
        1f to GeminiViolet,
        center = Offset(width * 0.3f, width * 0.28f),
        radius = width * 0.95f
    )
}
