package com.jeepchief.dh.core.util

import android.graphics.Color

object RarityChecker {
    fun convertColor(rarity: String) : Int {
        return when(rarity) {
            "커먼" -> Color.parseColor("#FFFFFF")
            "언커먼" -> Color.parseColor("#68D5ED")
            "레어" -> Color.parseColor("#B36BFF")
            "유니크" -> Color.parseColor("#FF00FF")
            "에픽" -> Color.parseColor("#FFB400")
            "크로니클" -> Color.parseColor("#FF6666")
            "레전더리" -> Color.parseColor("#FF7800")
            "신화" -> Color.parseColor("#46EA7A")
            else -> Color.parseColor("#FFFFFF")
        }
    }

    fun convertRarityColor(rarity: String) : Long {
        return when(rarity) {
            "커먼" -> 0xFFFFFFFF
            "언커먼" -> 0xFF68D5ED
            "레어" -> 0xFFB36BFF
            "유니크" -> 0xFFFF00FF
            "에픽" -> 0xFFFFB400
            "크로니클" -> 0xFFFF6666
            "레전더리" -> 0xFFFF7800
            "신화" -> 0xFF46EA7A
            "태초" -> 0xFF65FFEF
            else -> 0xFFFFFFFF
        }
    }
}