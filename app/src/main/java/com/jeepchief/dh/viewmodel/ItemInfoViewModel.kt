package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.ItemSearchDTO
import com.jeepchief.dh.model.rest.dto.ItemsDTO
import kotlinx.coroutines.launch

class ItemInfoViewModel : ViewModel() {
    var dfService: DfService = RetroClient.getInstance().create(DfService::class.java)

    // item search
    private val _itemsSearch: MutableLiveData<ItemSearchDTO> by lazy { MutableLiveData<ItemSearchDTO>() }
    val itemsSearch: LiveData<ItemSearchDTO> get() = _itemsSearch

    fun getSearchItems(itemName: String, wordType: String, q: String) {
        viewModelScope.launch {
            _itemsSearch.value = dfService.getSearchItems(itemName, wordType, q)
        }
    }

    // Get item Detail info
    private val _itemInfo: MutableLiveData<ItemsDTO> by lazy { MutableLiveData<ItemsDTO>() }
    val itemInfo: LiveData<ItemsDTO> get() = _itemInfo

    fun getItemInfo(itemId: String) {
        viewModelScope.launch {
            _itemInfo.value = dfService.getItemInfo(itemId)
        }
    }
}