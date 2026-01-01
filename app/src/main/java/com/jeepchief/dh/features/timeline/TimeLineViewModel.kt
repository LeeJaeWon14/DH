package com.jeepchief.dh.features.timeline

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.repository.DhApiRepository
import com.jeepchief.dh.core.network.dto.TimeLineDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeLineViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val apiRepository: DhApiRepository
) : ViewModel() {

    // Get Timeline
    private val _timeLine = MutableStateFlow(TimeLineDTO())
    val timeLine: StateFlow<TimeLineDTO> = _timeLine
    fun getTimeLine(serverId: String, characterId: String) {
        viewModelScope.launch {
            _timeLine.value = apiRepository.getTimeLine(serverId, characterId)
        }
    }

    private val _itemSummary = MutableStateFlow(HashMap<String, List<ItemRows>>())
    val itemSummary = _itemSummary.asStateFlow()
    fun getTotalSummary() {
        val baseSummaryMap = _timeLine.value.timeline?.rows?.groupBy { it.date.substring(0, 10) } ?: return

        val itemSummaryMap = mutableMapOf<String, List<ItemRows>>()

        baseSummaryMap.forEach { (date, timeLineList) ->
            val tempList = mutableListOf<ItemRows>()
            timeLineList.filter {
                it.code in 500 until 600
                        || it.name.contains("획득")
            }.forEach { timeLine ->
                tempList.add(
                    ItemRows(
                        timeLine.data.itemId ?: "",
                        timeLine.data.itemName ?: "",
                        timeLine.data.itemRarity ?: ""
                    )
                )
            }

            itemSummaryMap.put(date, tempList)
        }

        _itemSummary.value = HashMap(itemSummaryMap.filter { it.value.isNotEmpty() }.toSortedMap())
    }

    private val _raidSummary  = MutableStateFlow(HashMap<String, List<String>>())
    val raidSummary = _raidSummary.asStateFlow()
    fun getRaidSummary() {
        val baseSummaryMap = _timeLine.value.timeline?.rows?.groupBy { it.date.substring(0, 10) } ?: return

        val raidSummaryMap = mutableMapOf<String, List<String>>()
        baseSummaryMap.forEach { (date, timeLineList) ->
            val tempList = mutableListOf<String>()
            timeLineList.forEach { timeLine ->
                when (timeLine.code) {
                    201 -> {
                        tempList.add(
                            String.format(
                                context.getString(R.string.timeline_clear_raid),
                                "${timeLine.data.raidName} - ${timeLine.data.modeName}"
                            )
                        )
                    }
                    209 -> {
                        tempList.add(
                            String.format(
                                context.getString(R.string.timeline_clear_region),
                                timeLine.data.regionName
                            )
                        )
                    }
                }
            }
            raidSummaryMap.put(date, tempList)
        }

        _raidSummary.value = HashMap(raidSummaryMap.filter { it.value.isNotEmpty() }.toSortedMap())
    }
}