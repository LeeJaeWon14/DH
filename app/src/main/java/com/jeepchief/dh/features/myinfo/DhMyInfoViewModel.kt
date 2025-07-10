package com.jeepchief.dh.features.myinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.AvatarDTO
import com.jeepchief.dh.core.network.dto.BuffEquipDTO
import com.jeepchief.dh.core.network.dto.CreatureDTO
import com.jeepchief.dh.core.network.dto.EquipmentDTO
import com.jeepchief.dh.core.network.dto.FlagDTO
import com.jeepchief.dh.core.network.dto.StatusDTO
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.core.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DhMyInfoViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
): ViewModel() {

    // Flag info
    private val _flag = MutableStateFlow(FlagDTO())
    val flag: StateFlow<FlagDTO> get() = _flag
    fun getFlag(serverId: String, characterId: String) {
        Log.d("getFlag()")
        flag.value.flag?.let {
            return
        }

        viewModelScope.launch {
            _flag.value = apiRepository.getFlag(serverId, characterId)
        }
    }

    // Creature info
    private val _creature = MutableStateFlow(CreatureDTO())
    val creature: StateFlow<CreatureDTO> get() = _creature
    fun getCreature(serverId: String, characterId: String) {
        Log.d("getCreature()")
        creature.value.creature?.let {
            return
        }

        viewModelScope.launch {
            _creature.value = apiRepository.getCreature(serverId, characterId)
        }
    }

    // Avatar info
    private val _avatar = MutableStateFlow(AvatarDTO())
    val avatar: StateFlow<AvatarDTO> = _avatar
    fun getAvatar(serverId: String, characterId: String) {
        Log.d("getAvatar()")
        avatar.value.avatar?.let {
            return
        }

        viewModelScope.launch {
            _avatar.value = apiRepository.getAvatar(serverId, characterId)
        }
    }

    // Equipment info
    private val _equipment = MutableStateFlow(EquipmentDTO())
    val equipment: StateFlow<EquipmentDTO> get() = _equipment
    fun getEquipment(serverId: String, characterId: String) {
        Log.d("getEquipment()")
        equipment.value.equipment?.let {
            return
        }

        viewModelScope.launch {
            _equipment.value = apiRepository.getEquipment(serverId, characterId)
        }
    }

    // Status info
    private val _status = MutableStateFlow(StatusDTO())
    val status: StateFlow<StatusDTO> = _status
    fun getStatus(serverId: String, characterId: String) {
        Log.d("getStatus()")
        status.value.status?.let {
            return
        }

        viewModelScope.launch {
            _status.value = apiRepository.getCharacterStatus(serverId, characterId)
        }
    }

    // Get Talisman
    private val _talisman = MutableStateFlow(TalismanDTO())
    val talisman: StateFlow<TalismanDTO> = _talisman
    fun getTalisman(serverId: String, characterId: String) {
        viewModelScope.launch {
            _talisman.value = apiRepository.getTalisman(serverId, characterId)
        }
    }

    // Get Buff Skill Equip
    private val _buffSkillEquip = MutableStateFlow(BuffEquipDTO())
    val buffSkillEquip: StateFlow<BuffEquipDTO> get() = _buffSkillEquip
    fun getBuffSkillEquip(serverId: String, characterId: String) {
        Log.d("getBuffSkillEquip()")
        buffSkillEquip.value.skill?.let {
            return
        }

        viewModelScope.launch {
            _buffSkillEquip.value = apiRepository.getBuffEquip(serverId, characterId)
        }
    }
}