package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusDTO(

    @Expose
    @SerializedName("characterId")
    val characterId: String = "",
    @Expose
    @SerializedName("characterName")
    val characterName: String = "",
    @Expose
    @SerializedName("level")
    val level: Int = 0,
    @Expose
    @SerializedName("jobId")
    val jobId: String = "",
    @Expose
    @SerializedName("jobGrowId")
    val jobGrowId: String = "",
    @Expose
    @SerializedName("jobName")
    val jobName: String = "",
    @Expose
    @SerializedName("jobGrowName")
    val jobGrowName: String = "",
    @Expose
    @SerializedName("adventureName")
    val adventureName: String = "",
    @Expose
    @SerializedName("buff")
    val buff: List<Buff>? = null,
    @Expose
    @SerializedName("status")
    val status: List<Status>? = null
)

data class Status(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("value")
    val value: String
)

data class Buff(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("level")
    val level: Int,
    @Expose
    @SerializedName("status")
    val status: List<Status>
)