package com.jeepchief.dh.core.repository

import com.jeepchief.dh.core.database.recent.RecentAuctionEntity
import com.jeepchief.dh.core.database.recent.RecentFameEntity
import com.jeepchief.dh.core.database.recent.RecentItemEntity
import com.jeepchief.dh.core.database.recent.RecentSearchDAO
import javax.inject.Inject

class DhRecentRepository @Inject constructor(
    private val recentSearchDAO: RecentSearchDAO
) {
    val allRecentItem = recentSearchDAO.getRecentItemSearch()

    suspend fun insertRecentItem(entity: RecentItemEntity) = recentSearchDAO.insertRecentItem(entity)

    suspend fun deleteRecentItem(entity: RecentItemEntity) = recentSearchDAO.deleteRecentItem(entity)

    val allRecentAuction = recentSearchDAO.getRecentAuction()

    suspend fun insertRecentAuction(entity: RecentAuctionEntity) = recentSearchDAO.insertRecentAuction(entity)

    suspend fun deleteRecentAuction(entity: RecentAuctionEntity) = recentSearchDAO.deleteRecentAuction(entity)

    val allRecentFame = recentSearchDAO.getRecentFame()

    suspend fun insertRecentFame(entity: RecentFameEntity) = recentSearchDAO.insertRecentFame(entity)

    suspend fun deleteRecentFame(entity: RecentFameEntity) = recentSearchDAO.deleteRecentFame(entity)
}