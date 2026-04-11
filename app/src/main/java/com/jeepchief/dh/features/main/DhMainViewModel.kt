package com.jeepchief.dh.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.characters.CharactersEntity
import com.jeepchief.dh.core.database.recent.RecentItemEntity
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.CharacterDTO
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.network.dto.ItemSearchDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.repository.DhCharacterRepository
import com.jeepchief.dh.core.repository.DhRecentRepository
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.launchSafety
import com.jeepchief.dh.features.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DhMainViewModel @Inject constructor(
    private val apiRepository: DhApiRepository,
    private val characterRepository: DhCharacterRepository,
    private val recentSearchRepository: DhRecentRepository
) : BaseViewModel() {
    private val _nowCharacterInfo = MutableStateFlow(CharacterRows())
    val nowCharacterInfo: StateFlow<CharacterRows> = _nowCharacterInfo
    fun setNowCharacterInfo(value: CharacterRows) {
        _nowCharacterInfo.value = value
    }

    // Characters info list
    private val _characters = MutableStateFlow(CharacterDTO(listOf()))
    val characters: StateFlow<CharacterDTO> get() = _characters
    fun getCharacters(serverId: String = "all", name: String) = launchSafety(
        onError = { emitMessage(it) }
    ) {
        Log.d("getCharacters()")
            _characters.value = apiRepository.getCharacters(serverId, name)
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

    fun updateCharacter(character: CharacterRows) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            characterRepository.updateCharacter(
                CharactersEntity(character)
            )
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

        launchSafety(
            onError = { emitMessage(it) }
        ) {
            _itemSearch.value = apiRepository.getSearchItems(itemName, wordType, qResult)
        }
    }

    fun initSearchItems() = viewModelScope.launch {
        _itemSearch.value = ItemSearchDTO()
    }

    private val _itemInfo = MutableStateFlow(ItemsDTO())
    val itemInfo: StateFlow<ItemsDTO> = _itemInfo
    fun getItemInfo(itemId: String) = launchSafety(
        onError = { emitMessage(it) }
    ) {
        Log.d("getItemInfo()")
        _itemInfo.value = apiRepository.getItemInfo(itemId)
    }

    private val _characterDefault = MutableSharedFlow<CharacterRows>()
    val characterDefault: SharedFlow<CharacterRows> = _characterDefault
    fun getCharacterDefault(serverId: String, characterId: String) = launchSafety(
        onError = { emitMessage(it) }
    ) {
        Log.d("getCharacterDefault()")
        _characterDefault.emit(apiRepository.getCharacterDefault(serverId, characterId))
    }

    val recentItems = recentSearchRepository.allRecentItem.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf()
    )

    fun insertRecentItem(itemName: String) = viewModelScope.launch {
        recentSearchRepository.insertRecentItem(
            RecentItemEntity(searchName = itemName, searchTime = System.currentTimeMillis())
        )
    }

    fun deleteRecentItem(recentItem: RecentItemEntity) = viewModelScope.launch {
        recentSearchRepository.deleteRecentItem(recentItem)
    }
}