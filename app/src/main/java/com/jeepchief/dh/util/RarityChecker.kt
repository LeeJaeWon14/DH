package com.jeepchief.dh.util

import android.graphics.Color

object RarityChecker {
    fun convertColor(rarity: String) : Int {
        return when(rarity) {
            "커먼" -> Color.parseColor("#000000")
            "언커먼" -> Color.parseColor("#68D5ED")
            "레어" -> Color.parseColor("#B36BFF")
            "유니크" -> Color.parseColor("#FF00FF")
            "에픽" -> Color.parseColor("#FFB400")
            "크로니클" -> Color.parseColor("#FF6666")
            "레전더리" -> Color.parseColor("#FF7800")
            else -> { 0 }
        }
    }
}