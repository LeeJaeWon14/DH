package com.jeepchief.dh.features.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.database.characters.CharacterDAO
import com.jeepchief.dh.core.database.characters.CharactersEntity
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.AvatarDTO
import com.jeepchief.dh.core.network.dto.BuffEquipDTO
import com.jeepchief.dh.core.network.dto.CharacterDTO
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.network.dto.CreatureDTO
import com.jeepchief.dh.core.network.dto.EquipmentDTO
import com.jeepchief.dh.core.network.dto.FlagDTO
import com.jeepchief.dh.core.network.dto.ItemSearchDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.network.dto.JobDTO
import com.jeepchief.dh.core.network.dto.ServerDTO
import com.jeepchief.dh.core.network.dto.StatusDTO
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import com.jeepchief.dh.core.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val apiRepository: DhApiRepository,
    private val characterDAO: CharacterDAO
) : ViewModel() {
//    val nowCharacterInfo: MutableLiveData<CharacterRows> by lazy { MutableLiveData<CharacterRows>() }

    private val _nowCharacterInfo = MutableStateFlow(CharacterRows())
    val nowCharacterInfo: StateFlow<CharacterRows> = _nowCharacterInfo
    fun setNowCharacterInfo(value: CharacterRows) {
        _nowCharacterInfo.value = value
    }

    // Server list (All server in game)
    private val _servers = MutableStateFlow(ServerDTO(listOf()))
    val servers: StateFlow<ServerDTO> get() = _servers
    fun getServerList() {
        viewModelScope.launch {
            _servers.value = apiRepository.getServers()
        }
    }

    // Characters info list
    private val _characters = MutableStateFlow(CharacterDTO(listOf()))
    val characters: StateFlow<CharacterDTO> get() = _characters
    fun getCharacters(serverId: String = "all", name: String) {
        viewModelScope.launch {
            _characters.value = apiRepository.getCharacters(serverId, name)
        }
    }

    // Job info list
    private val _jobs: MutableLiveData<JobDTO> by lazy { MutableLiveData<JobDTO>() }
    val jobs: LiveData<JobDTO> get() = _jobs

    fun getJobs() {
        viewModelScope.launch {
            _jobs.value = apiRepository.getJobs()
        }
    }

    // Status info
    private val _status = MutableStateFlow(StatusDTO())
    val status: StateFlow<StatusDTO> = _status
    fun getStatus() {
        Log.d("getStatus()")
        status.value.status?.let {
            return
        }

        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _status.value = apiRepository.getCharacterStatus(it.serverId, it.characterId)
            }
        }
    }

    // Equipment info
    private val _equipment = MutableStateFlow(EquipmentDTO())
    val equipment: StateFlow<EquipmentDTO> get() = _equipment
    fun getEquipment() {
        Log.d("getEquipment()")
        equipment.value.equipment?.let {
            return
        }

        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _equipment.value = apiRepository.getEquipment(it.serverId, it.characterId)
            }
        }
    }

    // Avatar info
    private val _avatar = MutableStateFlow(AvatarDTO())
    val avatar: StateFlow<AvatarDTO> = _avatar
    fun getAvatar() {
        Log.d("getAvatar()")
        avatar.value.avatar?.let {
            return
        }

        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _avatar.value = apiRepository.getAvatar(it.serverId, it.characterId)
            }
        }
    }

    // Creature info
    private val _creature = MutableStateFlow(CreatureDTO())
    val creature: StateFlow<CreatureDTO> get() = _creature
    fun getCreature() {
        Log.d("getCreature()")
        creature.value.creature?.let {
            return
        }

        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _creature.value = apiRepository.getCreature(it.serverId, it.characterId)
            }
        }
    }

    // Flag info
    private val _flag = MutableStateFlow(FlagDTO())
    val flag: StateFlow<FlagDTO> get() = _flag
    fun getFlag() {
        Log.d("getFlag()")
        flag.value.flag?.let {
            return
        }

        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _flag.value = apiRepository.getFlag(it.serverId, it.characterId)
            }
        }
    }

    // Get Character list
    private val _characterList = MutableStateFlow(listOf(CharactersEntity()))
    val characterList: StateFlow<List<CharactersEntity>> = _characterList
    fun getCharacterList(context: Context) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _characterList.value = characterDAO.getCharacters()
            }
        }
    }

    // Get Talisman
    private val _talisman = MutableStateFlow(TalismanDTO())
    val talisman: StateFlow<TalismanDTO> = _talisman
    fun getTalisman() {
        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _talisman.value = apiRepository.getTalisman(it.serverId, it.characterId)
            }
        }
    }

    // Get Buff Skill Equip
    private val _buffSkillEquip = MutableStateFlow(BuffEquipDTO())
    val buffSkillEquip: StateFlow<BuffEquipDTO> get() = _buffSkillEquip
    fun getBuffSkillEquip() {
        Log.d("getBuffSkillEquip()")
        buffSkillEquip.value.skill?.let {
            return
        }

        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _buffSkillEquip.value = apiRepository.getBuffEquip(it.serverId, it.characterId)
            }
        }
    }

    // Get Timeline
    private val _timeLine = MutableStateFlow(TimeLineDTO())
    val timeLine: StateFlow<TimeLineDTO> = _timeLine
    fun getTimeLine() {
        Log.d("getTImeLine()")
        viewModelScope.launch {
            nowCharacterInfo.value?.let {
                _timeLine.value = apiRepository.getTimeLine(it.serverId, it.characterId)
            }
        }
    }

    fun insertCharacter(character: CharacterRows) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                characterDAO.insertCharacter(
                    CharactersEntity(character)
                )
            }
        }
    }

//    fun getCharacterWithId(characterId: String): CharacterRows {
//
//    }

    private val _itemSearch = MutableStateFlow(ItemSearchDTO())
    val itemSearch: StateFlow<ItemSearchDTO> = _itemSearch
    fun getSearchItems(itemName: String, wordType: String, q: String) {
        Log.d("""
            getSearchItems()
            itemName: $itemName
            wordType: $wordType,
            q: $q
        """.trimIndent())
        viewModelScope.launch {
            _itemSearch.value = apiRepository.getSearchItems(itemName, wordType, q)
        }
    }

    private val _itemInfo = MutableStateFlow(ItemsDTO())
    val itemInfo: StateFlow<ItemsDTO> = _itemInfo
    fun getItemInfo(itemId: String) {
        viewModelScope.launch {
            _itemInfo.value = apiRepository.getItemInfo(itemId)
        }
    }
}