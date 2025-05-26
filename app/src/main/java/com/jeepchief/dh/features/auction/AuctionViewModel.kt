package com.jeepchief.dh.features.auction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.AuctionDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuctionViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
) : ViewModel() {
    // Get Auction
    private val _auction: MutableLiveData<AuctionDTO> by lazy { MutableLiveData<AuctionDTO>() }
    val auction: LiveData<AuctionDTO> get() = _auction

    fun getAuction(itemName: String) {
        viewModelScope.launch {
            _auction.value = apiRepository.getAuction(itemName)
        }
    }
}