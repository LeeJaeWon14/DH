package com.jeepchief.dh.features.character

import com.jeepchief.dh.core.network.dto.CharacterDTO
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.launchSafety
import com.jeepchief.dh.features.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
) : BaseViewModel() {
    // Characters info list
    private val _characters = MutableStateFlow(CharacterDTO(listOf()))
    val characters: StateFlow<CharacterDTO> get() = _characters
    fun getCharacters(serverId: String = "all", name: String) = launchSafety(
        onError = { emitMessage(it) }
    ) {
        Log.d("getCharacters()")
        _characters.value = apiRepository.getCharacters(serverId, name)
    }
}