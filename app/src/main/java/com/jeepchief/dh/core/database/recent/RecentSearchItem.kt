package com.jeepchief.dh.core.database.recent

data class RecentSearchItem(
    val searchName: String = "",
    val searchTime: Long = 0L
) {
    constructor(entity: RecentItemEntity) : this(
        entity.searchName,
        entity.searchTime
    )

    constructor(entity: RecentAuctionEntity) : this(
        entity.searchName,
        entity.searchTime
    )

    constructor(entity: RecentFameEntity) : this(
        entity.searchName,
        entity.searchTime
    )
}