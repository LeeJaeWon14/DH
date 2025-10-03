package com.jeepchief.dh.core.database.recent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDAO {
    @Query("SELECT * FROM RecentItemEntity")
    fun getRecentItemSearch() : Flow<List<RecentItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentItem(entity: RecentItemEntity)

    @Delete
    suspend fun deleteRecentItem(entity: RecentItemEntity)

    @Query("SELECT * FROM RecentAuctionEntity")
    fun getRecentAuction() : Flow<List<RecentAuctionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentAuction(entity: RecentAuctionEntity)

    @Delete
    suspend fun deleteRecentAuction(entity: RecentAuctionEntity)

    @Query("SELECT * FROM RecentFameEntity")
    fun getRecentFame() : Flow<List<RecentFameEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentFame(entity: RecentFameEntity)

    @Delete
    suspend fun deleteRecentFame(entity: RecentFameEntity)
}