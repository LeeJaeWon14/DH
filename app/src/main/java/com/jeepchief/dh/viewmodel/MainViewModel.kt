package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.*
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val mySimpleInfo: MutableLiveData<CharacterRows> by lazy { MutableLiveData<CharacterRows>() }
    var dfService: DfService = RetroClient.getInstance().create(DfService::class.java)


    // Server list
    private val _servers: MutableLiveData<ServerDTO> by lazy { MutableLiveData<ServerDTO>() }
    val servers: LiveData<ServerDTO> get() = _servers

    fun getServerList() {
        viewModelScope.launch {
            _servers.value = dfService.getServers()
        }
    }

    // Characters info list
    private val _characters: MutableLiveData<CharacterDTO> by lazy { MutableLiveData<CharacterDTO>() }
    val characters: LiveData<CharacterDTO> get() = _characters

    fun getCharacters(name: String) {
        viewModelScope.launch {
            _characters.value = dfService.getCharacters(name)
        }
    }

    // Job info list
    private val _jobs: MutableLiveData<JobDTO> by lazy { MutableLiveData<JobDTO>() }
    val jobs: LiveData<JobDTO> get() = _jobs

    fun getJobs() {
        viewModelScope.launch {
            _jobs.value = dfService.getJobs()
        }
    }

    // Status info
    private val _status: MutableLiveData<StatusDTO> by lazy { MutableLiveData<StatusDTO>() }
    val status: LiveData<StatusDTO> get() = _status

    fun getStatus() {
        viewModelScope.launch {
            mySimpleInfo.value?.let {
                _status.value = dfService.getCharacterStatus(it.serverId, it.characterId)
            }
        }
    }

    // Equipment info
    private val _equipment: MutableLiveData<EquipmentDTO> by lazy { MutableLiveData<EquipmentDTO>() }
    val equipment: LiveData<EquipmentDTO> get() = _equipment

    fun getEquipment() {
        viewModelScope.launch {
            mySimpleInfo.value?.let {
                _equipment.value = dfService.getEquipment(it.serverId, it.characterId)
            }
        }
    }

    // Avatar info
    private val _avatar: MutableLiveData<AvatarDTO> by lazy { MutableLiveData<AvatarDTO>() }
    val avatar: LiveData<AvatarDTO> get() = _avatar

    fun getAvatar() {
        viewModelScope.launch {
            mySimpleInfo.value?.let {
                _avatar.value = dfService.getAvatar(it.serverId, it.characterId)
            }
        }
    }

    // Creature info
    private val _creature: MutableLiveData<CreatureDTO> by lazy { MutableLiveData<CreatureDTO>() }
    val creature: LiveData<CreatureDTO> get() = _creature

    fun getCreature() {
        viewModelScope.launch {
            mySimpleInfo.value?.let {
                _creature.value = dfService.getCreature(it.serverId, it.characterId)
            }
        }
    }

    // Flag info
    private val _flag: MutableLiveData<FlagDTO> by lazy { MutableLiveData<FlagDTO>() }
    val flag: LiveData<FlagDTO> get() = _flag

    fun getFlag() {
        viewModelScope.launch {
            mySimpleInfo.value?.let {
                _flag.value = dfService.getFlag(it.serverId, it.characterId)
            }
        }
    }

    // item search
    private val _itemsSearch: MutableLiveData<ItemSearchDTO> by lazy { MutableLiveData<ItemSearchDTO>() }
    val itemsSearch: LiveData<ItemSearchDTO> get() = _itemsSearch

    fun getSearchItems(itemName: String) {
        viewModelScope.launch {
            _itemsSearch.value = dfService.getSearchItems(itemName)
        }
    }

    // Get item Detail info
    private val _itemInfo: MutableLiveData<ItemsDTO> by lazy { MutableLiveData<ItemsDTO>() }
    val itemInfo: LiveData<ItemsDTO> get() = _itemInfo

    fun getItemInfo(itemId: String) {
        viewModelScope.launch {
            _itemInfo.value = dfService.getItemInfo(itemId)
        }
    }
}