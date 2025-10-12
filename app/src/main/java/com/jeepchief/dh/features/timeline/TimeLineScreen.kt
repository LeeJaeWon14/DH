package com.jeepchief.dh.features.timeline

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.network.dto.TimeLineRows
import com.jeepchief.dh.core.util.convertRarityColor
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.DhMainViewModel
import com.jeepchief.dh.features.main.activity.MainActivity
import com.jeepchief.dh.features.main.navigation.BaseScreen
import com.jeepchief.dh.features.main.navigation.DhCircularProgress
import com.jeepchief.dh.features.main.navigation.Divider
import com.jeepchief.dh.features.main.navigation.ItemCard
import com.jeepchief.dh.features.main.navigation.ItemInfoDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest

@Composable
fun TimeLineScreen(
    timeLineViewModel: TimeLineViewModel = hiltViewModel(),
    stateViewModel: DhStateViewModel = hiltViewModel()
) {
    BaseScreen {
        val viewModel: DhMainViewModel = viewModel(LocalActivity.current as MainActivity)
        val timeLine by timeLineViewModel.timeLine.collectAsState()
        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
        val itemInfo by viewModel.itemInfo.collectAsState()
        val isShowingNotFoundResult by remember { mutableStateOf(false) }
        var isShowingSummaryDialog by remember { mutableStateOf(false) }
        val itemSummaryList = remember { mutableListOf<ItemRows>() }


        LaunchedEffect(Unit) {
            viewModel.nowCharacterInfo.collectLatest {
                timeLineViewModel.getTimeLine(it.serverId, it.characterId)
            }
        }

        timeLine.timeline?.let {
            Spacer(Modifier.height(5.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val groupedDate = it.rows.groupBy { it.date.substring(0, 10) }
                groupedDate.forEach { (date, rows) ->
                    item(key = date) {
                        Text(
                            modifier = Modifier.clickable { isShowingSummaryDialog = true },
                            text = date,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(20f, TextUnitType.Sp)
                        )
                        Spacer(Modifier.height(5.dp))
                    }
                    items(rows) { row ->
                        row.data.also {
                            if(it.itemId?.isNotEmpty() == true && it.itemName?.isNotEmpty() == true) {
                                itemSummaryList.add(
                                    ItemRows(
                                        itemId = it.itemId ?: "",
                                        itemName = it.itemName ?: "",
                                        itemRarity = it.itemRarity ?: ""
                                    )
                                )
                            }
                        }

                        TimeLineCard(row) {
                            row.data.itemId?.let { id ->
                                stateViewModel.setIsShowingItemInfoDialog(true)
                                viewModel.getItemInfo(id)
                            }
                        }
                        Spacer(Modifier.height(5.dp))
                    }
                }

            }

        } ?: DhCircularProgress()

        if(isShowingItemInfoDialog) {
            ItemInfoDialog(itemInfo, stateViewModel)
        }

        if(isShowingNotFoundResult) {
            Toast.makeText(LocalContext.current, LocalContext.current.getString(R.string.error_msg_not_found_timeline), Toast.LENGTH_SHORT).show()
            backDispatcher?.onBackPressed()
            return@BaseScreen
        }

        if(isShowingSummaryDialog) {
            AlertDialog(
                onDismissRequest = { isShowingSummaryDialog = false },
                confirmButton = {

                },
                text = {
                    LazyColumn {
                        val taechoList = itemSummaryList.filter { it.itemRarity == "태초" }
                        item {

                            Text(
                                text = "태초 개수 : ${taechoList.size}",
                                color = Color.White
                            )
                        }
                        items(taechoList) {
                            ItemCard(it)
                        }

                        val epicList = itemSummaryList.filter { it.itemRarity == "에픽" }
                        item {
                            Divider()
                            Text(
                                text = "에픽 개수 : ${epicList.size}",
                                color = Color.White
                            )
                        }
                        items(epicList) {
                            ItemCard(it)
                        }

                        val legendaryList = itemSummaryList.filter { it.itemRarity == "레전더리" }
                        item {
                            Divider()
                            Text(
                                text = "레전더리 개수 : ${legendaryList.size}",
                                color = Color.White
                            )
                        }
                        items(legendaryList) {
                            ItemCard(it)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun TimeLineCard(row: TimeLineRows, isClickable: Boolean = false, clickCallback: () -> Unit = {}) {
    val descMap = getTimeLineDesc(LocalContext.current, row)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(onClick = clickCallback)
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(
                text = row.date.split(" ")[1],
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = descMap["desc"] ?: "",
                color = Color.White
            )
            Text(
                text = descMap["detail"] ?: "",
                color = Color(descMap["rarity"]?.toLong() ?: 0xFFFFFFFF),
            )
        }
    }
}

fun getTimeLineDesc(context: Context, row: TimeLineRows) : Map<String, String> {
    val resultMap = mutableMapOf<String, String>().also { it.put("desc", row.name) }
    when(row.code) {
        101 -> resultMap.put("detail", "아라드에서 모험 시작")
        102 -> {}
        103 -> resultMap.put("detail", String.format(context.getString(R.string.timeline_job_grown), row.data.jobGrowName))
        104 -> resultMap.put("detail", context.getString(R.string.timeline_max_level))
        105 -> {}
        201 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_clear_raid),
                    "${row.data.raidName} - ${row.data.modeName}"
                )
            )
        }
        202 -> {}
        203 -> {}
        204 -> {}
        205 -> {}
        206 -> {}
        207 -> {}
        208 -> {}
        209 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_clear_region),
                    row.data.regionName
                )
            )
        }
        301 -> {}
        401 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_succession),
                    "강화",
                    row.data.itemName?.plus(" (+${row.data.after})")
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        402 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_succession),
                    "증폭",
                    row.data.itemName?.plus(" (+${row.data.after})")
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        403 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_refine),
                    row.data.itemName,
                    row.data.after,
                    if(row.data.result == true) { "성공" } else { "실패" }
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        404 -> {}
        405 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_succession),
                    "새김",
                    row.data.itemName?.plus(" (+${row.data.reinforce})")
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        406 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_succession),
                    "계승",
                    row.data.itemName?.plus(" (+${row.data.reinforce})")
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        501 -> {}
        502 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_legendary),
                    row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        504 -> {
            resultMap.put("desc", row.name)
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_item_pot),
                    row.data.channelName, row.data.channelNo, row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        505 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_item_dungeon),
                    row.data.channelName, row.data.channelNo, row.data.dungeonName, row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        506 -> {}
        507 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_item_raid),
                    row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        508 -> {}
        509 -> {}
        510 -> {}
        511 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_item_upgrade),
                    row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        512 -> {}
        513 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_item_dungeon_no_channel),
                    row.data.dungeonName, row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        514 -> {
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_succession),
                    "제작서",
                    row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        515 -> {}
        516 -> {
            resultMap.put(
                "detail",
                String.format(
                    "%s 아이템 초월",
                    row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        517 -> {}
        518 -> {}
        519 -> {}
        520 -> {}
    }

    return resultMap
}