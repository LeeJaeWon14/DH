package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FameDTO(
    @Expose
    @SerializedName("fame")
    val fame: Fame? = null,

    @Expose
    @SerializedName("rows")
    val rows: List<FameRows>? = null
)

data class Fame(
    @Expose
    @SerializedName("min")
    val min: Int,

    @Expose
    @SerializedName("max")
    val max: Int
)

data class FameRows(
    @Expose
    @SerializedName("serverId")
    val serverId: String,

    @Expose
    @SerializedName("characterId")
    val characterId: String,

    @Expose
    @SerializedName("characterName")
    val characterName: String,

    @Expose
    @SerializedName("level")
    val level: Int,

    @Expose
    @SerializedName("jobId")
    val jobId: String,

    @Expose
    @SerializedName("jobName")
    val jobName: String,

    @Expose
    @SerializedName("jobGrowId")
    val jobGrowId: String,

    @Expose
    @SerializedName("jobGrowName")
    val jobGrowName: String,

    @Expose
    @SerializedName("fame")
    val fame: Int
)