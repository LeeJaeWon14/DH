package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.TimeLineDTO
import kotlinx.coroutines.launch

class TimeLineViewModel : ViewModel() {
    var dfService: DfService = RetroClient.getInstance().create(DfService::class.java)

    // Get Timeline
    private val _timeLine: MutableLiveData<TimeLineDTO> by lazy { MutableLiveData<TimeLineDTO>() }
    val timeLine: LiveData<TimeLineDTO> get() = _timeLine

    fun getTimeLine(serverId: String, characterId: String) {
        viewModelScope.launch {
            _timeLine.value = dfService.getTimeLine(serverId, characterId)
        }
    }
}