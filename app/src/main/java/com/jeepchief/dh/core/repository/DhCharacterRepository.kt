package com.jeepchief.dh.core.repository

import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.characters.CharactersEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DhCharacterRepository @Inject constructor(
    private val characterDAO: CharacterDAO
) {
    val allCharacters: Flow<List<CharactersEntity>> = characterDAO.getCharacters()

    suspend fun insertCharacter(entity: CharactersEntity) = characterDAO.insertCharacter(entity)

    suspend fun deleteCharacter(characterId: String) = characterDAO.deleteCharacter(characterId)
}