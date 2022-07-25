package com.jeepchief.dh.model.database.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CharacterDAO {
    @Query("SELECT * FROM CharactersEntity")
    fun getCharacters() : List<CharactersEntity>

    @Insert
    fun insertCharacter(entity: CharactersEntity)

    @Query("DELETE FROM CharactersEntity WHERE characterId = :characterId")
    fun deleteCharacter(characterId: String)

    @Query("SELECT * FROM CharactersEntity WHERE characterId = :characterId")
    fun selectCharacterId(characterId: String) : CharactersEntity?
}