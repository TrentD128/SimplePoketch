package com.trent.simplepoketch.presentation

import android.content.Context
import com.trent.simplepoketch.R
import com.trent.simplepoketch.presentation.theme.getDrawableResource

object BitmapFont {
    fun getCharToDrawableMap(themeIndex: Int, context: Context): Map<Char, Int> {
        return mapOf(
            '0' to getDrawableResource(themeIndex, "time0", context),
            '1' to getDrawableResource(themeIndex, "time1", context),
            '2' to getDrawableResource(themeIndex, "time2", context),
            '3' to getDrawableResource(themeIndex, "time3", context),
            '4' to getDrawableResource(themeIndex, "time4", context),
            '5' to getDrawableResource(themeIndex, "time5", context),
            '6' to getDrawableResource(themeIndex, "time6", context),
            '7' to getDrawableResource(themeIndex, "time7", context),
            '8' to getDrawableResource(themeIndex, "time8", context),
            '9' to getDrawableResource(themeIndex, "time9", context),
            ':' to getDrawableResource(themeIndex, "timecolon", context),
            'A' to getDrawableResource(themeIndex, "time_am", context),
            'P' to getDrawableResource(themeIndex, "time_pm", context)
        )
    }
}

object DBitmapFont {
    fun getCharToDrawableMap(themeIndex: Int, context: Context): Map<Char, Int> {
        // Map debug font characters to drawable resources
        return mapOf(
            '0' to getDrawableResource(themeIndex, "detime0", context),
            '1' to getDrawableResource(themeIndex, "detime1", context),
            '2' to getDrawableResource(themeIndex, "detime2", context),
            '3' to getDrawableResource(themeIndex, "detime3", context),
            '4' to getDrawableResource(themeIndex, "detime4", context),
            '5' to getDrawableResource(themeIndex, "detime5", context),
            '6' to getDrawableResource(themeIndex, "detime6", context),
            '7' to getDrawableResource(themeIndex, "detime7", context),
            '8' to getDrawableResource(themeIndex, "detime8", context),
            '9' to getDrawableResource(themeIndex, "detime9", context),
            ':' to getDrawableResource(themeIndex, "detimecolon", context),
            'A' to getDrawableResource(themeIndex, "detime_am", context),
            'P' to getDrawableResource(themeIndex, "detime_pm", context)
        )
    }
}

object StepBitmapFont {
    fun getCharToDrawableMap(themeIndex: Int, context: Context): Map<Char, Int> {
        return mapOf(
            '0' to getDrawableResource(themeIndex, "step0", context),
            '1' to getDrawableResource(themeIndex, "step1", context),
            '2' to getDrawableResource(themeIndex, "step2", context),
            '3' to getDrawableResource(themeIndex, "step3", context),
            '4' to getDrawableResource(themeIndex, "step4", context),
            '5' to getDrawableResource(themeIndex, "step5", context),
            '6' to getDrawableResource(themeIndex, "step6", context),
            '7' to getDrawableResource(themeIndex, "step7", context),
            '8' to getDrawableResource(themeIndex, "step8", context),
            '9' to getDrawableResource(themeIndex, "step9", context)
        )
    }
}
