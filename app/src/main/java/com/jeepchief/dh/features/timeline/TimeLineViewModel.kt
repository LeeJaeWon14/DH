package com.jeepchief.dh.features.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
) : ViewModel() {

    // Get Timeline
    private val _timeLine = MutableStateFlow(TimeLineDTO())
    val timeLine: StateFlow<TimeLineDTO> = _timeLine
    fun getTimeLine(serverId: String, characterId: String) {
        viewModelScope.launch {
            _timeLine.value = apiRepository.getTimeLine(serverId, characterId)
        }
    }
}