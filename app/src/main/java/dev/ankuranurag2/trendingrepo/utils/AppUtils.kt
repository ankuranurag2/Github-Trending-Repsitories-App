package dev.ankuranurag2.trendingrepo.utils

import android.graphics.Color as AndroidColor
import androidx.compose.ui.graphics.Color

object AppUtils {
    fun getComposeColorFromHex(colorString: String): Color {
        return Color(AndroidColor.parseColor(colorString))
    }
}