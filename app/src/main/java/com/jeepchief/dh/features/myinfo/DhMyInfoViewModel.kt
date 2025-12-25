package com.jeepchief.dh.features.myinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.AvatarDTO
import com.jeepchief.dh.core.network.dto.BuffEquipDTO
import com.jeepchief.dh.core.network.dto.CreatureDTO
import com.jeepchief.dh.core.network.dto.EquipmentDTO
import com.jeepchief.dh.core.network.dto.FlagDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.network.dto.MistAssimilationDTO
import com.jeepchief.dh.core.network.dto.StatusDTO
import com.jeepchief.dh.core.network.dto.TalismanDTO
import com.jeepchief.dh.core.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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

    /**
     * Emblem은 부위 당 2~3개가 들어가기 때문에
     * API Response를 명시적으로 모두 받고 Dialog 출력을 위해
     * Flag 추가
     */
    private val _emblemsInfoTrigger = MutableStateFlow(false)
    val emblemsInfoTrigger = _emblemsInfoTrigger.asStateFlow()
    fun initEmblemTrigger() {
        _emblemsInfoTrigger.value = false
        _emblems.value = emptyList()
    }

    // emblems info
    private val _emblems = MutableStateFlow<List<ItemsDTO>>(emptyList())
    val emblems = _emblems.asStateFlow()
    fun getEmblems(itemIds: List<String>) = viewModelScope.launch {
        itemIds.forEach { itemId ->
            val res = apiRepository.getItemInfo(itemId)
            _emblems.update { it + res }

            if(_emblems.value.size == itemIds.size) {
                _emblemsInfoTrigger.value = true
            }
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

    private val _buffSkillAvatar = MutableStateFlow(BuffEquipDTO())
    val buffSkillAvatar = _buffSkillAvatar.asStateFlow()
    fun getBuffSkillAvatar(serverId: String, characterId: String) = viewModelScope.launch {
        Log.d("getBuffSkillAvatar()")
        buffSkillAvatar.value.skill?.let { return@launch }

        _buffSkillAvatar.value = apiRepository.getBuffAvatar(serverId, characterId)
    }

    private val _buffSkillCreature = MutableStateFlow(BuffEquipDTO())
    val buffSKillCreature = _buffSkillCreature.asStateFlow()
    fun getBuffSkillCreature(serverId: String, characterId: String) = viewModelScope.launch {
        Log.d("getBuffSkillCreature()")
        buffSKillCreature.value.skill?.let { return@launch }

        _buffSkillCreature.value = apiRepository.getBuffCreature(serverId, characterId)
    }

    private val _mistAssimilation = MutableStateFlow(MistAssimilationDTO())
    val mistAssimilation = _mistAssimilation.asStateFlow()
    fun getMistAssimilation(serverId: String, characterId: String) = viewModelScope.launch {
        _mistAssimilation.value = apiRepository.getMistAssimilation(serverId, characterId)
    }
}