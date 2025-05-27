package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AvatarDTO(

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
    @SerializedName("avatar")
    val avatar: List<Avatar>? = null
)

data class Avatar(
    @Expose
    @SerializedName("slotId")
    val slotId: String,
    @Expose
    @SerializedName("slotName")
    val slotName: String,
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String,
    @Expose
    @SerializedName("clone")
    val clone: Clone,
    @Expose
    @SerializedName("random")
    val random: Random,
    @Expose
    @SerializedName("optionAbility")
    val optionAbility: String,
    @Expose
    @SerializedName("emblems")
    val emblems: List<Emblems>
)

data class Emblems(
    @Expose
    @SerializedName("slotNo")
    val slotNo: Int,
    @Expose
    @SerializedName("slotColor")
    val slotColor: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String
)

data class Random(
    @Expose
    @SerializedName("itemId")
    val itemId: Int?,
    @Expose
    @SerializedName("itemName")
    val itemName: String?
)
data class Clone(
    @Expose
    @SerializedName("itemId")
    val itemId: String?,
    @Expose
    @SerializedName("itemName")
    val itemName: String?
)
