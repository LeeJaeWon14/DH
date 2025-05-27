package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemSearchDTO(

    @Expose
    @SerializedName("rows")
    var rows: List<ItemRows>? = null
)

data class ItemRows(
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
    var itemAvailableLevel: Int
) {
    constructor(dto: ItemsDTO) : this(
        dto.itemId,
        dto.itemName,
        dto.itemRarity,
        dto.itemType,
        dto.itemTypeDetail,
        dto.itemAvailableLevel
    )

    constructor(equipment: Equipment) : this(
        equipment.itemId,
        equipment.itemName,
        equipment.itemRarity,
        equipment.itemType,
        equipment.itemTypeDetail,
        equipment.itemAvailableLevel
    )

    constructor(avatar: Avatar) : this(
        avatar.itemId,
        avatar.itemName,
        avatar.itemRarity,
        "",
        "",
        0
    )
}