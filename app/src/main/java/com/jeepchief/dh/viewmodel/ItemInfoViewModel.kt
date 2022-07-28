package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import com.jeepchief.dh.util.Log
import kotlinx.coroutines.launch

class ItemInfoViewModel : ViewModel() {
    var dfService: DfService = RetroClient.getInstance().create(DfService::class.java)

    // Get item Detail info
    private val _itemInfo: MutableLiveData<ItemsDTO> by lazy { MutableLiveData<ItemsDTO>() }
    val itemInfo: LiveData<ItemsDTO> get() = _itemInfo

    fun getItemInfo(itemId: String) {
        viewModelScope.launch {
            Log.e("called itemInfo function")
            _itemInfo.value = dfService.getItemInfo(itemId)
        }
    }
}