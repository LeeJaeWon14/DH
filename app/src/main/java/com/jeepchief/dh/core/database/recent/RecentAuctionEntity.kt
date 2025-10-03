package com.jeepchief.dh.core.database.recent

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentAuctionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,

    @ColumnInfo
    val searchName: String = "",

    @ColumnInfo
    val searchTime: Long = 0L
)