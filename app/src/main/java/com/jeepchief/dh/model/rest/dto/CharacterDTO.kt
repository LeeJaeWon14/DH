package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CharacterDTO(

    @Expose
    @SerializedName("rows")
    var characterRows: List<CharacterRows>
)

data class CharacterRows(
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
    val jobGrowName: String
)