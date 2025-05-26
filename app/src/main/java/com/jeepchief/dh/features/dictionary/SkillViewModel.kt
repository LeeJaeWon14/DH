package com.jeepchief.dh.features.dictionary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.SkillDTO
import com.jeepchief.dh.core.network.dto.SkillInfoDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkillViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
) : ViewModel() {
    var jobName: String = ""
    var jobId: String = ""

    //Get Skills
    private val _skills: MutableLiveData<SkillDTO> by lazy { MutableLiveData<SkillDTO>() }
    val skills: LiveData<SkillDTO> get() = _skills

    fun getSkills(jobId: String, jobGrowId: String) {
        viewModelScope.launch {
            _skills.value = apiRepository.getSkills(jobId, jobGrowId)
        }
    }

    // Get Skill Info
    private val _skillInfo: MutableLiveData<SkillInfoDTO> by lazy { MutableLiveData<SkillInfoDTO>() }
    val skillInfo: LiveData<SkillInfoDTO> get() = _skillInfo

    fun getSkillInfo(jobId: String, SkillId: String) {
        viewModelScope.launch {
            _skillInfo.value = apiRepository.getSkillInfo(jobId, SkillId)
        }
    }
}