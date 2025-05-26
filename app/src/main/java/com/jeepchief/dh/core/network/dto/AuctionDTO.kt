package com.jeepchief.dh.core.network.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AuctionDTO(
    @Expose
    @SerializedName("rows")
    var rows: List<AuctionRows>
)

data class AuctionRows(
    @Expose
    @SerializedName("auctionNo")
    var auctionNo: Int,
    @Expose
    @SerializedName("regDate")
    val regDate: String,
    @Expose
    @SerializedName("expireDate")
    val expireDate: String,
    @Expose
    @SerializedName("itemId")
    val itemId: String,
    @Expose
    @SerializedName("itemName")
    val itemName: String,
    @Expose
    @SerializedName("itemAvailableLevel")
    var itemAvailableLevel: Int,
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
    @SerializedName("refine")
    var refine: Int,
    @Expose
    @SerializedName("reinforce")
    var reinforce: Int,
    @Expose
    @SerializedName("amplificationName")
    val amplificationName: String,
    @Expose
    @SerializedName("adventureFame")
    var adventureFame: Int,
    @Expose
    @SerializedName("count")
    var count: Int,
    @Expose
    @SerializedName("price")
    var price: Int,
    @Expose
    @SerializedName("currentPrice")
    var currentPrice: Int,
    @Expose
    @SerializedName("unitPrice")
    var unitPrice: Int,
    @Expose
    @SerializedName("averagePrice")
    var averagePrice: Int
) : Serializable
