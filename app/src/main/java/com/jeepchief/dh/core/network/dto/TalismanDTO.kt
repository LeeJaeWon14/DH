package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TalismanDTO(

    @Expose
    @SerializedName("characterId")
    val characterId: String = "",
    @Expose
    @SerializedName("characterName")
    val characterName: String = "",
    @Expose
    @SerializedName("level")
    var level: Int = 0,
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
    @SerializedName("talismans")
    var talismans: List<Talismans>? = null
)

data class Talismans(
    @Expose
    @SerializedName("talisman")
    var talisman: Talisman,
    @Expose
    @SerializedName("runes")
    var runes: List<Runes>
)

data class Runes(
    @Expose
    @SerializedName("slotNo")
    var slotNo: Int,
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String
)

data class Talisman(
    @Expose
    @SerializedName("slotNo")
    var slotNo: Int,
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("runeTypes")
    var runeTypes: List<String>
)
