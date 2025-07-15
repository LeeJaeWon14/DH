package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemsDTO(

    @Expose
    @SerializedName("itemId")
    val itemId: String = "",
    @Expose
    @SerializedName("itemName")
    val itemName: String = "",
    @Expose
    @SerializedName("itemRarity")
    val itemRarity: String = "",
    @Expose
    @SerializedName("itemType")
    val itemType: String = "",
    @Expose
    @SerializedName("itemTypeDetail")
    val itemTypeDetail: String = "",
    @Expose
    @SerializedName("itemAvailableLevel")
    var itemAvailableLevel: Int = 0,
    @Expose
    @SerializedName("itemObtainInfo")
    val itemObtainInfo: String = "",
    @Expose
    @SerializedName("itemExplain")
    val itemExplain: String = "",
    @Expose
    @SerializedName("itemExplainDetail")
    val itemExplainDetail: String = "",
    @Expose
    @SerializedName("itemFlavorText")
    val itemFlavorText: String = "",
    @Expose
    @SerializedName("setItemId")
    val setItemId: String = "",
    @Expose
    @SerializedName("setItemName")
    val setItemName: String = "",
    @Expose
    @SerializedName("itemStatus")
    var itemStatus: List<ItemStatus>? = null,
    @Expose
    @SerializedName("growInfo")
    val growInfo: GrowInfo? = null,
    @Expose
    @SerializedName("fixedOption")
    val fixedOption: FixedOption? = null,
    @Expose
    @SerializedName("fusionOption")
    val fusionOption: FusionOption? = null,
    @Expose
    @SerializedName("jobs")
    val jobs: List<Job>? = null,
    @Expose
    @SerializedName("fame")
    val fame: Int = 0
)

data class Job(
    @Expose
    @SerializedName("jobId")
    val jobId: String,
    @Expose
    @SerializedName("jobName")
    val jobName: String
)

data class FusionOption(
    @Expose
    @SerializedName("options")
    val options: List<Options>
)

data class FixedOption(
    @Expose
    @SerializedName("damage")
    val damage: Float,
    @Expose
    @SerializedName("buff")
    val buff: Float,
    @Expose
    @SerializedName("explain")
    val explain: String,
    @Expose
    @SerializedName("explainDetail")
    val explainDetail: String
)

data class ItemStatus(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("value")
    var value: String
)

data class GrowInfo(
    @Expose
    @SerializedName("total")
    val total: Total,
    @Expose
    @SerializedName("options")
    val options: List<Options>
)

data class Total(
    @Expose
    @SerializedName("damage")
    val damage: Int,
    @Expose
    @SerializedName("buff")
    val buff: Int,
    @Expose
    @SerializedName("level")
    val level: Int
)

data class Options(
    @Expose
    @SerializedName("level")
    val level: Int,
    @Expose
    @SerializedName("expRate")
    val expRate: Int,
    @Expose
    @SerializedName("explain")
    val explain: String,
    @Expose
    @SerializedName("explainDetail")
    val explainDetail: String?,
    @Expose
    @SerializedName("damage")
    val damage: Int,
    @Expose
    @SerializedName("buff")
    val buff: Int
)
