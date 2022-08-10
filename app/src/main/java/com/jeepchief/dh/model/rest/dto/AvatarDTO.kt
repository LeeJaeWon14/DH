package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AvatarDTO(

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
    @SerializedName("avatar")
    var avatar: List<Avatar>
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
    var clone: Clone,
    @Expose
    @SerializedName("random")
    var random: Random,
    @Expose
    @SerializedName("optionAbility")
    val optionAbility: String,
    @Expose
    @SerializedName("emblems")
    var emblems: List<Emblems>
)

data class Emblems(
    @Expose
    @SerializedName("slotNo")
    var slotNo: Int,
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
    var itemId: Int?,
    @Expose
    @SerializedName("itemName")
    var itemName: String?
)
data class Clone(
    @Expose
    @SerializedName("itemId")
    var itemId: String?,
    @Expose
    @SerializedName("itemName")
    var itemName: String?
)
