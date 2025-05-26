package com.jeepchief.dh.features.myinfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.ItemSearchDTO
import com.jeepchief.dh.core.network.dto.ItemsDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemInfoViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
): ViewModel() {
    // item search
    private val _itemsSearch: MutableLiveData<ItemSearchDTO> by lazy { MutableLiveData<ItemSearchDTO>() }
    val itemsSearch: LiveData<ItemSearchDTO> get() = _itemsSearch

    fun getSearchItems(itemName: String, wordType: String, q: String) {
        viewModelScope.launch {
            _itemsSearch.value = apiRepository.getSearchItems(itemName, wordType, q)
        }
    }

    // Get item Detail info
    private val _itemInfo: MutableLiveData<ItemsDTO> by lazy { MutableLiveData<ItemsDTO>() }
    val itemInfo: LiveData<ItemsDTO> get() = _itemInfo

    fun getItemInfo(itemId: String) {
        viewModelScope.launch {
            _itemInfo.value = apiRepository.getItemInfo(itemId)
        }
    }
}