package com.jeepchief.dh.features.timeline

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
) : ViewModel() {
    // Get Timeline
    val _timeLine: MutableLiveData<TimeLineDTO> by lazy { MutableLiveData<TimeLineDTO>() }
    val timeLine: LiveData<TimeLineDTO> get() = _timeLine

    fun getTimeLine(serverId: String, characterId: String) {
        viewModelScope.launch {
            _timeLine.value = apiRepository.getTimeLine(serverId, characterId)
        }
    }
}