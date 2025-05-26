package com.jeepchief.dh.core.database.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CharacterDAO {
    @Query("SELECT * FROM CharactersEntity")
    fun getCharacters() : List<CharactersEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCharacter(entity: CharactersEntity)

    @Query("DELETE FROM CharactersEntity WHERE characterId = :characterId")
    fun deleteCharacter(characterId: String)

    @Query("SELECT * FROM CharactersEntity WHERE characterId = :characterId")
    fun selectCharacterId(characterId: String) : CharactersEntity?
}