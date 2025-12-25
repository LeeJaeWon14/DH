package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.SerializedName

data class MistAssimilationDTO(
    @SerializedName("serverId")
    val serverId: String = "",

    @SerializedName("characterId")
    val characterId: String = "",

    @SerializedName("characterName")
    val characterName: String = "",

    @SerializedName("level")
    val level: Int = 0,

    @SerializedName("jobId")
    val jobId: String = "",

    @SerializedName("jobGrowId")
    val jobGrowId: String = "",

    @SerializedName("jobName")
    val jobName: String = "",

    @SerializedName("jobGrowName")
    val jobGrowName: String = "",

    @SerializedName("fame")
    val fame: Int = 0,

    @SerializedName("adventureName")
    val adventureName: String = "",

    @SerializedName("guildId")
    val guildId: String = "",

    @SerializedName("guildName")
    val guildName: String = "",

    @SerializedName("mistAssimilation")
    val mistAssimilation: MistAssimilation = MistAssimilation()
)

data class MistAssimilation(
    @SerializedName("level")
    val level: Int = 0,

    @SerializedName("expRate")
    val expRate: String = "",

    @SerializedName("status")
    val status: List<Status> = emptyList()
)