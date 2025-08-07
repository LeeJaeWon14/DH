package com.jeepchief.dh.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.characters.CharactersEntity
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.CharacterDTO
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.network.dto.ItemSearchDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.repository.DhCharacterRepository
import com.jeepchief.dh.core.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DhMainViewModel @Inject constructor(
    private val apiRepository: DhApiRepository,
    private val characterRepository: DhCharacterRepository
) : ViewModel() {
    private val _nowCharacterInfo = MutableStateFlow(CharacterRows())
    val nowCharacterInfo: StateFlow<CharacterRows> = _nowCharacterInfo
    fun setNowCharacterInfo(value: CharacterRows) {
        _nowCharacterInfo.value = value
    }

    // Characters info list
    private val _characters = MutableStateFlow(CharacterDTO(listOf()))
    val characters: StateFlow<CharacterDTO> get() = _characters
    fun getCharacters(serverId: String = "all", name: String) {
        viewModelScope.launch {
            _characters.value = apiRepository.getCharacters(serverId, name)
        }
    }

    fun insertCharacter(character: CharacterRows) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                characterRepository.insertCharacter(
                    CharactersEntity(character)
                )
            }
        }
    }

    val allCharacters: StateFlow<List<CharactersEntity>> = characterRepository.allCharacters.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf()
    )

    fun deleteCharacter(characterId: String) {
        Log.d("deleteCharacter() > $characterId")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                characterRepository.deleteCharacter(characterId)
            }
        }
    }

    private val _itemSearch = MutableStateFlow(ItemSearchDTO())
    val itemSearch: StateFlow<ItemSearchDTO> = _itemSearch
    fun getSearchItems(itemName: String, wordType: String, q: String) {
        val qResult = "minLevel:0,maxLevel:0,rarity:$q"
        Log.d("""
            getSearchItems()
            itemName: $itemName
            wordType: $wordType,
            q: $qResult
        """.trimIndent())
        viewModelScope.launch {
            _itemSearch.value = apiRepository.getSearchItems(itemName, wordType, qResult)
        }
    }

    private val _itemInfo = MutableStateFlow(ItemsDTO())
    val itemInfo: StateFlow<ItemsDTO> = _itemInfo
    fun getItemInfo(itemId: String) {
        Log.d("getItemInfo()")

        viewModelScope.launch {
            _itemInfo.value = apiRepository.getItemInfo(itemId)
        }
    }

    private val _characterDefault = MutableStateFlow(CharacterRows())
    val characterDefault: StateFlow<CharacterRows> = _characterDefault
    fun getCharacterDefault(serverId: String, characterId: String) {
        Log.d("getCharacterDefault()")

        viewModelScope.launch {
            _characterDefault.value = apiRepository.getCharacterDefault(serverId, characterId)
        }
    }
}