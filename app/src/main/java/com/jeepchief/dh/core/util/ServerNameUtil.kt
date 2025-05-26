package com.jeepchief.dh.core.util

object ServerNameUtil {
    fun convertServerName(server: String) : String {
        return when(server) {
            "cain" -> "카인"
            "diregie" -> "디레지에"
            "prey" -> "프레이"
            "casillas" -> "카시야스"
            "hilder" -> "힐더"
            "anton" -> "안톤"
            "bakal" -> "바칼"
            else -> ""
        }
    }
}