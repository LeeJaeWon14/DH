package com.jeepchief.dh.features.character

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.CharacterDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
): ViewModel() {
    // Characters info list
    private val _characters = MutableStateFlow(CharacterDTO(listOf()))
    val characters: StateFlow<CharacterDTO> get() = _characters
    fun getCharacters(serverId: String = "all", name: String) {
        viewModelScope.launch {
            _characters.value = apiRepository.getCharacters(serverId, name)
        }
    }
}