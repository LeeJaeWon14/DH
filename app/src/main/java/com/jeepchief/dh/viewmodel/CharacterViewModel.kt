package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.CharacterDTO
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    var dfService: DfService = RetroClient.getInstance().create(DfService::class.java)

    // Characters info list
    private val _characters: MutableLiveData<CharacterDTO> by lazy { MutableLiveData<CharacterDTO>() }
    val characters: LiveData<CharacterDTO> get() = _characters

    fun getCharacters(serverId: String = "all", name: String) {
        viewModelScope.launch {
            _characters.value = dfService.getCharacters(serverId, name)
        }
    }
}