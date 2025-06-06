package com.jeepchief.dh.core.database.metadata

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ServersDAO {
    @Query("SELECT * FROM ServersEntity")
    fun selectServers() : List<ServersEntity>

    @Insert
    fun insertServers(entity: ServersEntity)

    @Query("SELECT * FROM ServersEntity WHERE serverId = :serverId")
    fun getTargetServer(serverId: String) : ServersEntity
}