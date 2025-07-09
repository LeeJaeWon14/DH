package com.jeepchief.dh.features.main

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class DhMainStateViewModel @Inject constructor() : ViewModel() {
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
}