package com.jeepchief.dh.model.rest.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ItemSearchDTO(

    @Expose
    @SerializedName("rows")
    var rows: List<ItemRows>
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
)