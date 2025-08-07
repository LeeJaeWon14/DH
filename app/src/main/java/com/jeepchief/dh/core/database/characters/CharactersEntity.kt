package com.jeepchief.dh.core.database.characters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeepchief.dh.core.network.dto.CharacterRows

@Entity
data class CharactersEntity(
    @ColumnInfo(name = "serverId")
    val serverId: String = "",

    @ColumnInfo(name = "characterId")
    val characterId: String = "",

    @ColumnInfo(name = "characterName")
    val characterName: String = "",

    @ColumnInfo(name = "level")
    val level: Int = 0,

    @ColumnInfo(name = "jobId")
    val jobId: String = "",

    @ColumnInfo(name = "jobGrowId")
    val jobGrowId: String = "",

    @ColumnInfo(name = "jobName")
    val jobName: String = "",

    @ColumnInfo(name = "jobGrowName")
    val jobGrowName: String = "",

    @ColumnInfo(name = "fame")
    val fame: Int = 0,

    @ColumnInfo(name = "guildName")
    val guildName: String = "",

    @ColumnInfo(name = "adventureName")
    val adventureName: String = "",

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
        row.fame,
        row.guildName,
        row.adventureName,
        0
    )

    fun toRow(): CharacterRows =
        CharacterRows(
            serverId, characterId, characterName, level, jobId, jobGrowId, jobName, jobGrowName, fame, guildName, adventureName
        )
}