package com.jeepchief.dh.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.model.rest.DfService
import com.jeepchief.dh.model.rest.RetroClient
import com.jeepchief.dh.model.rest.dto.AuctionDTO
import kotlinx.coroutines.launch

class AuctionViewModel : ViewModel() {
    private val dfService = RetroClient.getInstance().create(DfService::class.java)

    // Get Auction
    private val _auction: MutableLiveData<AuctionDTO> by lazy { MutableLiveData<AuctionDTO>() }
    val auction: LiveData<AuctionDTO> get() = _auction

    fun getAuction(itemName: String) {
        viewModelScope.launch {
            _auction.value = dfService.getAuction(itemName)
        }
    }
}