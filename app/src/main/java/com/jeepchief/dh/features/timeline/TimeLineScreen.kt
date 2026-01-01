package com.jeepchief.dh.features.timeline

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
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
        val itemInfo by viewModel.itemInfo.collectAsState()
        val itemSummary by timeLineViewModel.itemSummary.collectAsState()
        val raidSummary by timeLineViewModel.raidSummary.collectAsState()

        var clickedDate by remember { mutableStateOf("") }

        val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
        var isShowingNotFoundResult by remember { mutableStateOf(false) }
        var isDailyShowingItemSummaryDialog by remember { mutableStateOf(false) }
        var isShowingItemSummaryDialog by remember { mutableStateOf(false) }
        var isShowingRaidSummaryDialog by remember { mutableStateOf(false) }


        LaunchedEffect(Unit) {
            viewModel.nowCharacterInfo.collectLatest {
                timeLineViewModel.getTimeLine(it.serverId, it.characterId)
            }
        }

        LaunchedEffect(timeLine) {
            timeLineViewModel.run {
                getTotalSummary()
                getRaidSummary()
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalTabContainer(
                "아이템 요약" to { isShowingItemSummaryDialog = true },
                "레이드/레기온 요약" to { isShowingRaidSummaryDialog = true }
            )

            timeLine.timeline?.let {
                Spacer(Modifier.height(5.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    val groupedDate = it.rows.groupBy { it.date.substring(0, 10) }
                    groupedDate.forEach { (date, rows) ->
                        item(key = date) {
                            Text(
                                modifier = Modifier.clickable {
                                    isDailyShowingItemSummaryDialog = true
                                    clickedDate = date
                                },
                                text = date,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = TextUnit(20f, TextUnitType.Sp)
                            )
                            Spacer(Modifier.height(5.dp))
                        }
                        items(rows) { row ->
                            TimeLineCard(row) {
                                row.data.itemId?.let { id ->
                                    stateViewModel.setIsShowingItemInfoDialog(true)
                                    viewModel.getItemInfo(id)
                                }
                            }
                            Spacer(Modifier.height(5.dp))
                        }
                        item {
                            Spacer(Modifier.height(15.dp))
                        }
                    }
                }

            } ?: DhCircularProgress()
        }

        if(isShowingItemInfoDialog) {
            ItemInfoDialog(itemInfo, stateViewModel)
        }

        if(isShowingNotFoundResult) {
            Toast.makeText(LocalContext.current, LocalContext.current.getString(R.string.error_msg_not_found_timeline), Toast.LENGTH_SHORT).show()
            backDispatcher?.onBackPressed()
        }

        if(isDailyShowingItemSummaryDialog) {
            val list = itemSummary.get(clickedDate) ?: run {
                Toast.makeText(LocalContext.current, "이 날에는 아이템 획득 기록이 없습니다.", Toast.LENGTH_SHORT).show()
                return@BaseScreen
            }

            DailyItemSummaryDialog(clickedDate, list) {
                isDailyShowingItemSummaryDialog = false
            }
        }

        if(isShowingItemSummaryDialog) {
            ItemSummaryDialog(itemSummary) {
                isShowingItemSummaryDialog = false
            }
        }

        if(isShowingRaidSummaryDialog) {
            RaidSummaryDialog(raidSummary) {
                isShowingRaidSummaryDialog = false
            }
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
            .padding(10.dp)
            .clickable(onClick = clickCallback)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = row.date.split(" ")[1].plus(" | "),
                fontSize = TextUnit(20f, TextUnitType.Sp),
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = descMap["desc"] ?: "",
                color = Color.White
            )
        }
        descMap["detail"]?.let { detail ->
            Text(
                text = detail,
                color = Color(descMap["rarity"]?.toLong() ?: 0xFFFFFFFF),
            )
        }
    }
}

@Composable
fun ItemSummaryDialog(
    itemSummaryMap: Map<String, List<ItemRows>>,
    onDismiss: () -> Unit
) {
    var taechoCount = 0
    var epicCount = 0
    var legendaryCount = 0
    var totalCount = 0

    SideEffect {
        var tempTCount = 0
        var tempECount = 0
        var tempLCount = 0
        itemSummaryMap.forEach { (date, list) ->
            tempTCount += list.count { it.itemRarity == "태초" }
            tempECount += list.count { it.itemRarity == "에픽" }
            tempLCount += list.count { it.itemRarity == "레전더리" }
        }

        taechoCount = tempTCount; epicCount = tempECount; legendaryCount = tempLCount
        totalCount = taechoCount + epicCount + legendaryCount
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text("닫기", color = Color.White)
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                if(totalCount != 0) {
                    item {
                        Text(
                            text = "총 획득 개수: ${totalCount}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = """
                            태초: $taechoCount
                            에픽: $epicCount
                            레전더리: $legendaryCount
                        """.trimIndent(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(5.dp))
                        Divider()
                    }


                    itemSummaryMap.toSortedMap(compareByDescending { it }).forEach { (date, list) ->
                        item {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = date,
                                color = Color.White
                            )
                        }
                        items(list.distinct()) {
                            ItemCard(it)
                        }
                    }
                }
                else {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "아이템 획득 기록이 없습니다.",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun DailyItemSummaryDialog(
    clickedDate: String,
    itemSummaryList: List<ItemRows>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text("닫기", color = Color.White)
            }
        },
        text = {
            LazyColumn {
                item {
                    Text(
                        text = clickedDate,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(15f, TextUnitType.Sp)
                    )
                }

                itemSummaryList.filter { it.itemRarity == "태초" }.let { taechoList ->
                    if(taechoList.isEmpty()) return@let

                    item { Spacer(Modifier.height(10.dp)) }
                    items(taechoList) {
                        ItemCard(it)
                    }
                    item {
                        Text(
                            text = "태초 ${taechoList.size}개",
                            color = Color.White
                        )

                        Spacer(Modifier.height(10.dp))
                    }
                }

                itemSummaryList.filter { it.itemRarity == "에픽" }.let { epicList ->
                    if(epicList.isEmpty()) return@let

                    item { Spacer(Modifier.height(10.dp)) }
                    items(epicList) {
                        ItemCard(it)
                    }
                    item {
                        Text(
                            text = "에픽 ${epicList.size}개",
                            color = Color.White
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }

                itemSummaryList.filter { it.itemRarity == "레전더리" }.let { legendaryList ->
                    if(legendaryList.isEmpty()) return@let

                    item { Spacer(Modifier.height(10.dp)) }
                    items(legendaryList) {
                        ItemCard(it)
                    }
                    item {
                        Text(
                            text = "레전더리 ${legendaryList.size}개",
                            color = Color.White
                        )
                        Spacer(Modifier.height(10.dp))
                    }
                }
            }
        }
    )
}

@Composable
fun HorizontalTabContainer(vararg tabContent: Pair<String, () -> Unit>) {
    Spacer(Modifier.height(5.dp))
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        items(tabContent) {
            OutlinedButton(
                onClick = it.second
            ) {
                Text(
                    text = it.first,
                    color = Color.White
                )
            }
            Spacer(Modifier.width(5.dp))
        }
    }
}

@Composable
fun RaidSummaryDialog(
    raidSummaryMap: Map<String, List<String>>,
    onDismiss: () -> Unit
) {
    var raidCount = 0
    var regionCount = 0
    var totalCount = 0

    SideEffect {
        var tempRaidCount = 0
        var tempRegionCount = 0

        raidSummaryMap.forEach { (date, list) ->
            tempRaidCount += list.filter { it.contains("레이드") }.count()
            tempRegionCount += list.filter { it.contains("레기온") }.count()
        }
        raidCount = tempRaidCount; regionCount = tempRegionCount
        totalCount = raidCount + regionCount
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onDismiss
            ) {
                Text(text = "닫기", color = Color.White)
            }
        },
        text = {
            LazyColumn {
                if(totalCount != 0) {
                    item {
                        Text(
                            text = "레이드/레기온 클리어 횟수: ${totalCount}회",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = """
                            레이드: ${raidCount}회
                            레기온: ${regionCount}회
                        """.trimIndent(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Divider()
                    }

                    raidSummaryMap.forEach { (date, list) ->
                        item {
                            Spacer(Modifier.height(10.dp))
                            Text(
                                text = date,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        items(list) {
                            Text(
                                text = it,
                                color = Color.White
                            )
                            Spacer(Modifier.height(10.dp))
                        }
                    }
                }
                else {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "레이드, 레기온 클리어 기록이 없습니다.",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    )
}

fun getTimeLineDesc(context: Context, row: TimeLineRows) : Map<String, String> {
    val resultMap = mutableMapOf<String, String>().also { it.put("desc", row.name) }
    fun generateItemDesc(desc: String) {
        resultMap.put("detail", desc)
        resultMap.put("rarity", row.data.itemRarity.orEmpty().convertRarityColor().toString())
    }

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
        401 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "강화",
                row.data.itemName?.plus(" (+${row.data.after})")
            )
        )
        402 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "증폭",
                row.data.itemName?.plus(" (+${row.data.after})")
            )
        )
        403 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_refine),
                row.data.itemName,
                row.data.after,
                if(row.data.result == true) { "성공" } else { "실패" }
            )
        )
        404 -> {}
        405 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "새김",
                row.data.itemName?.plus(" (+${row.data.reinforce})")
            )
        )
        406 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "계승",
                row.data.itemName?.plus(" (+${row.data.reinforce})")
            )
        )
        501 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_item_bongja),
                row.data.itemName
            )
        )
        502 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_legendary),
                row.data.itemName
            )
        )
        504 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_item_pot),
                row.data.channelName, row.data.channelNo, row.data.itemName
            )
        )
        505 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_item_dungeon),
                row.data.channelName, row.data.channelNo, row.data.dungeonName, row.data.itemName
            )
        )
        506 -> {}
        507 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_item_raid),
                row.data.itemName
            )
        )
        508 -> {}
        509 -> {}
        510 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "강화", row.data.itemName
            )
        )
        511 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_item_upgrade),
                row.data.itemName
            )
        )
        512 -> {}
        513 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_get_item_dungeon_no_channel),
                row.data.dungeonName, row.data.itemName
            )
        )
        514 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "제작서",
                row.data.itemName
            )
        )
        515 -> {}
        516 -> generateItemDesc(
            String.format(
                "%s 아이템 초월",
                row.data.itemName
            )
        )
        517 -> {}
        518 -> {}
        519 -> {}
        520 -> generateItemDesc(
            String.format(
                context.getString(R.string.timeline_item_succession),
                "장비제작",
                row.data.itemName
            )
        )
    }

    return resultMap
}