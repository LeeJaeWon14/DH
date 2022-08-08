package com.jeepchief.dh.model.database.metadata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ServersEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "serverId")
    var serverId: String,

    @ColumnInfo(name = "serverName")
    var serverName: String
)
