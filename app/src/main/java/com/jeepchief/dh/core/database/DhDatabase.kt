package com.jeepchief.dh.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.characters.CharactersEntity
import com.jeepchief.dh.core.database.metadata.ServersDAO
import com.jeepchief.dh.core.database.metadata.ServersEntity
import com.jeepchief.dh.core.database.recent.RecentAuctionEntity
import com.jeepchief.dh.core.database.recent.RecentFameEntity
import com.jeepchief.dh.core.database.recent.RecentItemEntity
import com.jeepchief.dh.core.database.recent.RecentSearchDAO

@Database(entities = [ServersEntity::class, CharactersEntity::class, RecentAuctionEntity::class, RecentItemEntity::class, RecentFameEntity::class], version = 5, exportSchema = false)
abstract class DhDatabase : RoomDatabase() {
    abstract fun getServersDAO() : ServersDAO
    abstract fun getCharactersDAO() : CharacterDAO
    abstract fun getRecentSearchDAO() : RecentSearchDAO

    companion object {
        private var instance: DhDatabase? = null
        @Synchronized
        fun getInstance(context: Context) : DhDatabase {
            instance?.let {
                return it
            } ?: run {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DhDatabase::class.java,
                    "DH.db"
                ).fallbackToDestructiveMigration().build()
                return instance!!
            }
        }
    }
}