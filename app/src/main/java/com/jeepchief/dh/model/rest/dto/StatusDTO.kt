package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StatusDTO(

    @Expose
    @SerializedName("characterId")
    val characterId: String,
    @Expose
    @SerializedName("characterName")
    val characterName: String,
    @Expose
    @SerializedName("level")
    var level: Int,
    @Expose
    @SerializedName("jobId")
    val jobId: String,
    @Expose
    @SerializedName("jobGrowId")
    val jobGrowId: String,
    @Expose
    @SerializedName("jobName")
    val jobName: String,
    @Expose
    @SerializedName("jobGrowName")
    val jobGrowName: String,
    @Expose
    @SerializedName("adventureName")
    val adventureName: String,
    @Expose
    @SerializedName("buff")
    var buff: List<Buff>,
    @Expose
    @SerializedName("status")
    var status: List<Status>
)

data class Status(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("value")
    var value: String
)

data class Buff(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("level")
    var level: Int,
    @Expose
    @SerializedName("status")
    var status: List<Status>
)