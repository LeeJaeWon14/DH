package com.jeepchief.dh.features.fame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.database.recent.RecentFameEntity
import com.jeepchief.dh.core.database.recent.RecentSearchItem
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.FameDTO
import com.jeepchief.dh.core.network.dto.JobDTO
import com.jeepchief.dh.core.repository.DhRecentRepository
import com.jeepchief.dh.core.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DhFameViewModel @Inject constructor(
    private val apiRepository: DhApiRepository,
    private val recentSearchRepository: DhRecentRepository
) : ViewModel() {
    // Get Fame
    private val _fame = MutableStateFlow(FameDTO())
    val fame: StateFlow<FameDTO> = _fame
    fun getFame(fame: Int, jobId: String, jobGrowId: String) {
        viewModelScope.launch {
            _fame.value = apiRepository.getFame(fame, jobId, jobGrowId)
        }
    }

    // Job info list
    private val _jobs = MutableStateFlow(JobDTO())
    val jobs: StateFlow<JobDTO> = _jobs
    fun getJobs() {
        viewModelScope.launch {
            _jobs.value = apiRepository.getJobs()
        }
    }

    val recentFames = recentSearchRepository.allRecentFame.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        listOf()
    )

    fun insertRecentFame(searchName: String) = viewModelScope.launch {
        recentSearchRepository.insertRecentFame(
            RecentFameEntity(searchName = searchName, searchTime = System.currentTimeMillis())
        )
    }

    fun deleteRecentFame(recentFameEntity: RecentFameEntity) = viewModelScope.launch {
        recentSearchRepository.deleteRecentFame(recentFameEntity)
    }

    private val _characterDefault = MutableSharedFlow<CharacterRows>()
    val characterDefault: SharedFlow<CharacterRows> = _characterDefault
    fun getCharacterDefault(serverId: String, characterId: String) {
        Log.d("getCharacterDefault()")

        viewModelScope.launch {
            _characterDefault.emit(apiRepository.getCharacterDefault(serverId, characterId))
        }
    }
}