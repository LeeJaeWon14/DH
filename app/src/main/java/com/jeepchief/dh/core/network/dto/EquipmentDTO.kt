package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EquipmentDTO(

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
    @SerializedName("equipment")
    val equipment: List<Equipment>? = null,
    @Expose
    @SerializedName("setItemInfo")
    val setItemInfo: List<SetItemInfo>? = null
)

data class Enchant(
    @Expose
    @SerializedName("status")
    val status: List<Status>
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
    val itemAvailableLevel: Int,
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
    val reinforce: Int,
    @Expose
    @SerializedName("itemGradeName")
    val itemGradeName: String,
    @Expose
    @SerializedName("refine")
    val refine: Int,
    @Expose
    @SerializedName("enchant")
    val enchant: Enchant? = null,
    @Expose
    @SerializedName("fusionOptions")
    val fusionOption: FusionOption? = null,
    @Expose
    @SerializedName("upgradeInfo")
    val upgradeInfo: UpgradeInfo? = null,
    @Expose
    @SerializedName("tune")
    val tune: List<Tune>? = null
)

data class UpgradeInfo(
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String
)

data class Tune(
    @Expose
    @SerializedName("level")
    val level: Int,
    @Expose
    @SerializedName("setPoint")
    val setPoint: Int
)

data class SetItemInfo(
    @Expose
    @SerializedName("setItemId")
    val setItemId: String,
    @Expose
    @SerializedName("setItemName")
    val setItemName: String,
    @Expose
    @SerializedName("setItemRarityName")
    val setItemRarityName: String,
    @Expose
    @SerializedName("active")
    val active: Active
)

data class Active(
    @Expose
    @SerializedName("explain")
    val explain: String? = null,
    @Expose
    @SerializedName("buffExplain")
    val buffExplain: String? = null,
    @Expose
    @SerializedName("status")
    val status: List<Status>,
    @Expose
    @SerializedName("setPoint")
    val setPoint: SetPoint
)

data class SetPoint(
    @Expose
    @SerializedName("current")
    val current: Int,
    @Expose
    @SerializedName("min")
    val min: Int,
    @Expose
    @SerializedName("max")
    val max: Int
)