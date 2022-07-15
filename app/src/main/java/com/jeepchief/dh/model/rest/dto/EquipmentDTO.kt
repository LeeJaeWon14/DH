package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EquipmentDTO(

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
    @SerializedName("equipment")
    var equipment: List<Equipment>,
    @Expose
    @SerializedName("setItemInfo")
    var setItemInfo: List<SetItemInfo>
)

data class Equipment(
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
    @SerializedName("itemType")
    val itemType: String,
    @Expose
    @SerializedName("itemTypeDetail")
    val itemTypeDetail: String,
    @Expose
    @SerializedName("itemAvailableLevel")
    var itemAvailableLevel: Int,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String,
    @Expose
    @SerializedName("setItemId")
    val setItemId: String,
    @Expose
    @SerializedName("setItemName")
    val setItemName: String,
    @Expose
    @SerializedName("reinforce")
    var reinforce: Int,
    @Expose
    @SerializedName("itemGradeName")
    val itemGradeName: String,
    @Expose
    @SerializedName("refine")
    var refine: Int
)

data class SetItemInfo(
    @Expose
    @SerializedName("setItemId")
    var setItemId: String,
    @Expose
    @SerializedName("setItemName")
    var setItemName: String,
    @Expose
    @SerializedName("slotInfo")
    var slotInfo: List<SlotInfo>
)

data class SlotInfo(
    @Expose
    @SerializedName("slotId")
    var slotId: String,
    @Expose
    @SerializedName("slotName")
    var slotName: String,
    @Expose
    @SerializedName("itemRarity")
    var itemRarity: String
)