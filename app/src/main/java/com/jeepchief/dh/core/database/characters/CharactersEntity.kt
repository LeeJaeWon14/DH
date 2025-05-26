package com.jeepchief.dh.core.database.characters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeepchief.dh.core.network.dto.CharacterRows

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
) {
    constructor(row: CharacterRows) : this(
        row.serverId,
        row.characterId,
        row.characterName,
        row.level,
        row.jobId,
        row.jobGrowId,
        row.jobName,
        row.jobGrowName,
        0
    )

    fun toRow(): CharacterRows =
        CharacterRows(
            serverId, characterId, characterName, level, jobId, jobGrowId, jobName, jobGrowName
        )
}