package com.jeepchief.dh.features.main

import androidx.lifecycle.ViewModel
import com.jeepchief.dh.DHApplication
import com.jeepchief.dh.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DhStateViewModel @Inject constructor(): ViewModel() {
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

    private val _jobExpanded = MutableStateFlow(false)
    val jobExpanded: StateFlow<Boolean> = _jobExpanded
    fun setJobExpanded(value: Boolean) {
        _jobExpanded.value = value
    }

    private val _jobGrowExpanded = MutableStateFlow(false)
    val jobGrowExpanded: StateFlow<Boolean> = _jobGrowExpanded
    fun setJobGrowExpanded(value: Boolean) {
        _jobGrowExpanded.value = value
    }

    private val _priceSort = MutableStateFlow(DHApplication.getAppContext().getString(R.string.text_auction_sort_desc))
    val priceSort: StateFlow<String> = _priceSort
    fun setPriceSort(value: String) {
        _priceSort.value = value
    }

    private val _isShowingAuctionSettingDialog = MutableStateFlow(false)
    val isShowingAuctionSettingDialog: StateFlow<Boolean> = _isShowingAuctionSettingDialog
    fun setIsShowingAuctionSettingDialog(value: Boolean) {
        _isShowingAuctionSettingDialog.value = value
    }

    private val _isShowingBottomSheet = MutableStateFlow(false)
    val isShowingBottomSheet: StateFlow<Boolean> = _isShowingBottomSheet
    fun setIsShowingBottomSheet(value: Boolean) {
        _isShowingBottomSheet.value = value
    }

    private val _isShowingSetItemInfoBottomSheet = MutableStateFlow(false)
    val isShowingSetItemInfoBottomSheet: StateFlow<Boolean> = _isShowingSetItemInfoBottomSheet
    fun setIsShowingSetItemInfoBottomSheet(value: Boolean) {
        _isShowingSetItemInfoBottomSheet.value = value
    }
}