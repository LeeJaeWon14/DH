package com.jeepchief.dh.features.auction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.core.network.DhApiRepository
import com.jeepchief.dh.core.network.dto.AuctionDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DhAuctionViewModel @Inject constructor(
    private val apiRepository: DhApiRepository
): ViewModel() {
    // Get Auction
    private val _auction = MutableStateFlow(AuctionDTO())
    val auction: StateFlow<AuctionDTO> get() = _auction
    fun getAuction(sort: String, itemName: String, q: String) {
//        val qResult = """
//            minLevel:<minLevel>,maxLevel:<maxLevel>,rarity:<rarity>,
//            reinforceTypeId:<reinforceTypeId>,minReinforce:<minReinforce>,maxReinforce:<maxReinforce>,
//            minRefine:<minRefine>,maxRefine:<maxRefine>,minFame:<minFame>,maxFame:<maxFame>
//        """.trimIndent()
        viewModelScope.launch {
            _auction.value = apiRepository.getAuction(sort, itemName, "rarity:$q")
        }
    }
}