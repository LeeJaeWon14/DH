package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.SkillDTO
import com.jeepchief.dh.model.rest.dto.SkillInfoDTO
import kotlinx.coroutines.launch

class SkillViewModel : ViewModel() {
    var dfService: DfService = RetroClient.getInstance().create(DfService::class.java)
    var jobName: String = ""
    var jobId: String = ""

    //Get Skills
    private val _skills: MutableLiveData<SkillDTO> by lazy { MutableLiveData<SkillDTO>() }
    val skills: LiveData<SkillDTO> get() = _skills

    fun getSkills(jobId: String, jobGrowId: String) {
        viewModelScope.launch {
            _skills.value = dfService.getSkills(jobId, jobGrowId)
        }
    }

    // Get Skill Info
    private val _skillInfo: MutableLiveData<SkillInfoDTO> by lazy { MutableLiveData<SkillInfoDTO>() }
    val skillInfo: LiveData<SkillInfoDTO> get() = _skillInfo

    fun getSkillInfo(jobId: String, SkillId: String) {
        viewModelScope.launch {
            _skillInfo.value = dfService.getSkillInfo(jobId, SkillId)
        }
    }
}