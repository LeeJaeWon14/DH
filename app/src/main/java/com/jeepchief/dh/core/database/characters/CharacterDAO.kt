package com.jeepchief.dh.core.database.characters

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDAO {
    @Query("SELECT * FROM CharactersEntity")
    fun getCharacters() : Flow<List<CharactersEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCharacter(entity: CharactersEntity)

    @Query("DELETE FROM CharactersEntity WHERE characterId = :characterId")
    suspend fun deleteCharacter(characterId: String)

    @Query("SELECT * FROM CharactersEntity WHERE characterId = :characterId")
    suspend fun selectCharacterId(characterId: String) : CharactersEntity?

    @Update
    suspend fun updateCharacter(entity: CharactersEntity): Int
}