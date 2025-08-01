package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TimeLineDTO(

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
    @SerializedName("adventureName")
    val adventureName: String = "",
    @Expose
    @SerializedName("guildId")
    val guildId: String = "",
    @Expose
    @SerializedName("guildName")
    val guildName: String = "",
    @Expose
    @SerializedName("timeline")
    var timeline: Timeline? = null
)

data class Timeline(
    @Expose
    @SerializedName("date")
    var date: Date,
    @Expose
    @SerializedName("next")
    val next: String,
    @Expose
    @SerializedName("rows")
    var rows: List<TimeLineRows>
)

data class TimeLineRows(
    @Expose
    @SerializedName("code")
    var code: Int,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("date")
    val date: String,
    @Expose
    @SerializedName("data")
    var data: Data
)

data class Data(
    @Expose
    @SerializedName("jobId")
    val jobId: String?,
    @Expose
    @SerializedName("jobName")
    val jobName: String?,
    @Expose
    @SerializedName("jobGrowId")
    val jobGrowId: String?,
    @Expose
    @SerializedName("jobGrowName")
    val jobGrowName: String?,
    @Expose
    @SerializedName("itemId")
    val itemId: String?,
    @Expose
    @SerializedName("itemName")
    val itemName: String?,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String?,
    @Expose
    @SerializedName("itemObtainInfo")
    val itemObtainInfo: ItemObtainInfo?,
    @Expose
    @SerializedName("channelName")
    val channelName: String?,
    @Expose
    @SerializedName("channelNo")
    val channelNo: String?,
    @Expose
    @SerializedName("dungeonName")
    val dungeonName: String?,
    @Expose
    @SerializedName("amplification")
    val amplification: Boolean,
    @Expose
    @SerializedName("reinforce")
    val reinforce: Int?,
    @Expose
    @SerializedName("refine")
    val refine: Int?,
    @Expose
    @SerializedName("after")
    val after: Int?,
    @Expose
    @SerializedName("result")
    val result: Boolean?,
    @Expose
    @SerializedName("raidName")
    val raidName: String?,
    @Expose
    @SerializedName("raidPartyName")
    val raidPartyName: String?,
    @Expose
    @SerializedName("modeName")
    val modeName: String?,
    @Expose
    @SerializedName("hard")
    val isHard: Boolean?,
    @Expose
    @SerializedName("regionName")
    val regionName: String?
)

data class Date(
    @Expose
    @SerializedName("start")
    val start: String,
    @Expose
    @SerializedName("end")
    val end: String
)

data class ItemObtainInfo(
    @Expose
    @SerializedName("obtainName")
    val obtainName: String,
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String
)