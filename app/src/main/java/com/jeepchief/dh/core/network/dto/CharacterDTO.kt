package com.jeepchief.dh.core.network.dto

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
    val serverId: String = "",
    @Expose
    @SerializedName("characterId")
    val characterId: String = "",
    @Expose
    @SerializedName("characterName")
    val characterName: String = "",
    @Expose
    @SerializedName("level")
    var level: Int = -1,
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
    @SerializedName("fame")
    val fame: Int = 0,
    @Expose
    @SerializedName("guildName")
    val guildName: String = "",
    @Expose
    @SerializedName("adventureName")
    val adventureName: String = ""
) {
    constructor(rows: FameRows) : this(
        serverId = rows.serverId,
        characterId = rows.characterId,
        characterName = rows.characterName,
        level = rows.level,
        jobId = rows.jobId,
        jobGrowId = rows.jobGrowId,
        jobName = rows.jobName,
        jobGrowName = rows.jobGrowName,
        fame = rows.fame
    )
}