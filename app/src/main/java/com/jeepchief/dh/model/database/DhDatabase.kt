package com.jeepchief.dh.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jeepchief.dh.model.database.metadata.ServersDAO
import com.jeepchief.dh.model.database.metadata.ServersEntity

@Database(entities = [ServersEntity::class], version = 1, exportSchema = false)
abstract class DhDatabase : RoomDatabase() {
    abstract fun getServersDAO() : ServersDAO

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