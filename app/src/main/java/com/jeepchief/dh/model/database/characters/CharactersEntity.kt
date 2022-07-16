package com.jeepchief.dh.model.database.characters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CharactersEntity(
    @ColumnInfo(name = "serverId")
    var serverId: String,

    @ColumnInfo(name = "characterId")
    var characterId: String,

    @ColumnInfo(name = "characterName")
    var characterName: String,

    @ColumnInfo(name = "level")
    var level: Int,

    @ColumnInfo(name = "jobId")
    var jobId: String,

    @ColumnInfo(name = "jobGrowId")
    var jobGrowId: String,

    @ColumnInfo(name = "jobName")
    var jobName: String,

    @ColumnInfo(name = "jobGrowName")
    var jobGrowName: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
