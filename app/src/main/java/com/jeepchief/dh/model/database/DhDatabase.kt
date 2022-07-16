package com.jeepchief.dh.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeepchief.dh.model.database.characters.CharacterDAO
import com.jeepchief.dh.model.database.characters.CharactersEntity
import com.jeepchief.dh.model.database.metadata.ServersDAO
import com.jeepchief.dh.model.database.metadata.ServersEntity

@Database(entities = [ServersEntity::class, CharactersEntity::class], version = 2, exportSchema = false)
abstract class DhDatabase : RoomDatabase() {
    abstract fun getServersDAO() : ServersDAO
    abstract fun getCharactersDAO() : CharacterDAO

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