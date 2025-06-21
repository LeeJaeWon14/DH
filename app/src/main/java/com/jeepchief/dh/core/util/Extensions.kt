package com.jeepchief.dh.core.util

import androidx.compose.ui.res.stringResource
import com.jeepchief.dh.DHApplication
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import java.text.DecimalFormat

fun String.convertServerName() : String {
    return when(this) {
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

fun String.convertRarityColor() : Long {
    return when(this) {
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

fun String.toWordType(): String {
    return when(this) {
        DHApplication.getAppContext().getString(R.string.text_word_type_front) -> NetworkConstants.WORD_TYPE_FRONT
        DHApplication.getAppContext().getString(R.string.text_word_type_match) -> NetworkConstants.WORD_TYPE_MATCH
        DHApplication.getAppContext().getString(R.string.text_word_type_full) -> NetworkConstants.WORD_TYPE_FULL
        else -> ""
    }
}

fun String.makeComma() : String {
    //소숫점이 존재하거나 천 단위 이하일 경우 생략
    if(this.contains(".") || this.length < 4) {
        return this
    }
    val formatter = DecimalFormat("###,###")
    return formatter.format(this.toLong()).plus("골드")
}

fun String.toSort(): String {
    return when(this) {
        DHApplication.getAppContext().getString(R.string.text_auction_sort_desc) -> "desc"
        DHApplication.getAppContext().getString(R.string.text_auction_sort_asc) -> "asc"
        else -> ""
    }
}