package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemsDTO(

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
    @SerializedName("itemType")
    val itemType: String,
    @Expose
    @SerializedName("itemTypeDetail")
    val itemTypeDetail: String,
    @Expose
    @SerializedName("itemAvailableLevel")
    var itemAvailableLevel: Int,
    @Expose
    @SerializedName("itemObtainInfo")
    val itemObtainInfo: String,
    @Expose
    @SerializedName("itemExplain")
    val itemExplain: String,
    @Expose
    @SerializedName("itemExplainDetail")
    val itemExplainDetail: String,
    @Expose
    @SerializedName("itemFlavorText")
    val itemFlavorText: String,
    @Expose
    @SerializedName("setItemId")
    val setItemId: String?,
    @Expose
    @SerializedName("setItemName")
    val setItemName: String?,
    @Expose
    @SerializedName("itemStatus")
    var itemStatus: List<ItemStatus>?,
    @Expose
    @SerializedName("growInfo")
    val growInfo: GrowInfo?,
    @Expose
    @SerializedName("dimensionCloisterInfo")
    var dimensionCloisterInfo: DimensionCloisterInfo?,           // 차원회랑
    @Expose
    @SerializedName("machineRevolutionInfo")
    var machineRevolutionInfo: MachineRevolutionInfo?,          // 기계혁명
    @Expose
    @SerializedName("ispinsInfo")                               // 이스핀즈
    var ispinsInfo: IspinsInfo?
)

data class IspinsInfo(
    @Expose
    @SerializedName("options")
    var options: List<Options>
)

data class MachineRevolutionInfo(
    @Expose
    @SerializedName("options")
    var options: List<Options>
)

data class DimensionCloisterInfo(
    @Expose
    @SerializedName("options")
    var options: List<Options>
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
    val expRate: Double,
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
