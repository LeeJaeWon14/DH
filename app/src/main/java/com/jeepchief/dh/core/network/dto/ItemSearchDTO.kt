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

    // myInfo(equipment) screen only
    val reinforce: Int = -1,
    val tuneLevel: Int = -1,
    val upgradeInfo: UpgradeInfo? = null
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
        equipment.itemAvailableLevel,
        equipment.reinforce,
        equipment.tune?.get(0)?.level ?: -1,
        equipment.upgradeInfo
    )

    constructor(avatar: Avatar) : this(
        avatar.itemId,
        avatar.itemName,
        avatar.itemRarity,
        "",
        "",
        1
    )

    constructor(equipment: BuffEquipment) : this(
        equipment.itemId,
        equipment.itemName,
        equipment.itemRarity,
        equipment.itemType,
        equipment.itemTypeDetail,
        equipment.itemAvailableLevel
    )

    constructor(flag: Flag) : this(
        flag.itemId,
        flag.itemName,
        flag.itemRarity,
        "",
        "",
        1
    )

    constructor(gem: Gems) : this(
        gem.itemId,
        gem.itemName,
        gem.itemRarity,
        "",
        "",
        1
    )

    constructor(creature: Creature) : this(
        creature.itemId,
        creature.itemName,
        creature.itemRarity,
        "",
        "",
        1
    )

    constructor(artifact: Artifact) : this(
        artifact.itemId,
        artifact.itemName,
        artifact.itemRarity,
        "",
        "",
        artifact.itemAvailableLevel
    )

    constructor(auctionRows: AuctionRows) : this(
        auctionRows.itemId,
        auctionRows.itemName,
        auctionRows.itemRarity,
        auctionRows.itemType,
        auctionRows.itemTypeDetail,
        auctionRows.itemAvailableLevel
    )
}