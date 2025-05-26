package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ServerDTO(
    @Expose
    @SerializedName("rows")
    val rows: List<Rows>
)

data class Rows(
    @Expose
    @SerializedName("serverId")
    val serverId: String = "",
    @Expose
    @SerializedName("serverName")
    val serverName: String = ""
)