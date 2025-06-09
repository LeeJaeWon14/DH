package com.jeepchief.dh.features.main

import androidx.lifecycle.ViewModel
import com.jeepchief.dh.DHApplication
import com.jeepchief.dh.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DhStateViewModel: ViewModel() {
    private val _isShowingCharacterSearchDialog = MutableStateFlow(false)
    val isShowingCharacterSearchDialog: StateFlow<Boolean> = _isShowingCharacterSearchDialog
    fun setIsShowingCharacterSearchDialog(value: Boolean) {
        _isShowingCharacterSearchDialog.value = value
    }

    private val _isShowingCharacterSelectDialog = MutableStateFlow(false)
    val isShowingCharacterSelectDialog: StateFlow<Boolean> = _isShowingCharacterSelectDialog
    fun setIsShowingCharacterSelectDialog(value: Boolean) {
        _isShowingCharacterSelectDialog.value = value
    }

    private val _isShowingExitDialog = MutableStateFlow(false)
    val isShowingExitDialog: StateFlow<Boolean> = _isShowingExitDialog
    fun setIsShowingExitDialog(value: Boolean) {
        _isShowingExitDialog.value = value
    }

    private val _isShowingAppBar = MutableStateFlow(true)
    val isShowingAppBar: StateFlow<Boolean> = _isShowingAppBar
    fun setIsShowingAppBar(value: Boolean) {
        _isShowingAppBar.value = value
    }

    private val _isShowingItemInfoDialog = MutableStateFlow(false)
    val isShowingItemInfoDialog: StateFlow<Boolean> = _isShowingItemInfoDialog
    fun setIsShowingItemInfoDialog(value: Boolean) {
        _isShowingItemInfoDialog.value = value
    }

    private val _isShowingSearchSettingDialog = MutableStateFlow(false)
    val isShowingSearchSettingDialog: StateFlow<Boolean> = _isShowingSearchSettingDialog
    fun setIsShowingSearchSettingDialog(value: Boolean) {
        _isShowingSearchSettingDialog.value = value
    }

    private val _isShowingCharacterAddDialog = MutableStateFlow(false)
    val isShowingCharacterAddDialog: StateFlow<Boolean> = _isShowingCharacterAddDialog
    fun setIsShowingCharacterAddDialog(value: Boolean) {
        _isShowingCharacterAddDialog.value = true
    }

    private val _isShowingCharacterRemoveDialog = MutableStateFlow(false)
    val isShowingCharacterRemoveDialog: StateFlow<Boolean> = _isShowingCharacterRemoveDialog
    fun setIsShowingCharacterRemoveDialog(value: Boolean) {
        _isShowingCharacterRemoveDialog.value = value
    }

    private val _isShowingAuctionResultDialog = MutableStateFlow(false)
    val isShowingAuctionResultDialog: StateFlow<Boolean> = _isShowingAuctionResultDialog
    fun setIsShowingAuctionResultDialog(value: Boolean) {
        _isShowingAuctionResultDialog.value = value
    }

    private val _isShowingFameInfoDialog = MutableStateFlow(true)
    val isShowingFameInfoDialog: StateFlow<Boolean> = _isShowingFameInfoDialog
    fun setIsShowingFameInfoDialog(value: Boolean) {
        _isShowingFameInfoDialog.value = value
    }

    private val _searchType = MutableStateFlow(DHApplication.getAppContext().getString(R.string.text_word_type_front))
    val searchType: StateFlow<String> = _searchType
    fun setSearchType(value: String) {
        _searchType.value = value
    }

    private val _rarityType = MutableStateFlow(DHApplication.getAppContext().getString(R.string.text_rarity_all))
    val rarityType: StateFlow<String> = _rarityType
    fun setRarityType(value: String) {
        _rarityType.value = value
    }

    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded
    fun setExpanded(value: Boolean) {
        _expanded.value = value
    }
}