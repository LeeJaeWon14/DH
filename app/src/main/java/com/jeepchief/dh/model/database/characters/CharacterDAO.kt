package com.jeepchief.dh.model.database.characters

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterDAO {
    @Query("SELECT * FROM CharactersEntity")
    fun getCharacters() : List<CharactersEntity>

    @Insert
    fun insertCharacter(entity: CharactersEntity)

    @Delete
    fun deleteCharacter(entity: CharactersEntity)

    @Query("SELECT * FROM CharactersEntity WHERE characterId = :characterId")
    fun selectCharacterId(characterId: String) : CharactersEntity?
}