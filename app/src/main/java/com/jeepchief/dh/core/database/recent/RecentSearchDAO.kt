package com.jeepchief.dh.core.database.recent

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchDAO {
    @Query("SELECT * FROM RecentItemEntity ORDER BY searchTime DESC")
    fun getRecentItemSearch() : Flow<List<RecentItemEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentItem(entity: RecentItemEntity)

    @Delete
    suspend fun deleteRecentItem(entity: RecentItemEntity)

    @Query("SELECT * FROM RecentAuctionEntity ORDER BY searchTime DESC")
    fun getRecentAuction() : Flow<List<RecentAuctionEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentAuction(entity: RecentAuctionEntity)

    @Delete
    suspend fun deleteRecentAuction(entity: RecentAuctionEntity)

    @Query("SELECT * FROM RecentFameEntity ORDER BY searchTime DESC")
    fun getRecentFame() : Flow<List<RecentFameEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecentFame(entity: RecentFameEntity)

    @Delete
    suspend fun deleteRecentFame(entity: RecentFameEntity)
}