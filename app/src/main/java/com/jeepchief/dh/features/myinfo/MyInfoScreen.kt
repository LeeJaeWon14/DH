package com.jeepchief.dh.features.myinfo

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.network.dto.Active
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.network.dto.Option
import com.jeepchief.dh.core.network.dto.SetItemInfo
import com.jeepchief.dh.core.network.dto.Status
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.convertRarityColor
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.DhMainViewModel
import com.jeepchief.dh.features.main.activity.MainActivity
import com.jeepchief.dh.features.main.navigation.BaseScreen
import com.jeepchief.dh.features.main.navigation.DhCircularProgress
import com.jeepchief.dh.features.main.navigation.DhModalBottomSheet
import com.jeepchief.dh.features.main.navigation.ItemCard
import com.jeepchief.dh.features.main.navigation.ItemInfoDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun MyInfoScreen(
    myInfoVieWModel: DhMyInfoViewModel = hiltViewModel(),
    stateViewModel: DhStateViewModel = hiltViewModel(),
    viewModel: DhMainViewModel = viewModel(LocalActivity.current as MainActivity)
) {
    BaseScreen(false) {
        val context = LocalContext.current
        val tabs = remember {
            listOf(
                context.getString(R.string.tab_status),
                context.getString(R.string.tab_equipment),
                context.getString(R.string.tab_avatar),
                context.getString(R.string.tab_buff_equipment),
                context.getString(R.string.tab_creature),
                context.getString(R.string.tab_gem)
            )
        }
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(pageCount = { tabs.size })
        val myInfo by viewModel.nowCharacterInfo.collectAsState()

        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.character_background),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
                GlideImage(
                    model = String.format(NetworkConstants.CHARACTER_URL, myInfo.serverId, myInfo.characterId, 0),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Fit
                )
            }
            ScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                edgePadding = 0.dp
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = TextUnit(15f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
            ) { page ->
                when (page) {
                    0 -> MyInfoStatus(viewModel, myInfoVieWModel)
                    1 -> MyInfoEquipment(viewModel, stateViewModel, myInfoVieWModel)
                    2 -> MyInfoAvatar(viewModel, stateViewModel, myInfoVieWModel)
                    3 -> MyInfoBuffEquipment(viewModel, stateViewModel, myInfoVieWModel)
                    4 -> MyInfoCreature(viewModel, stateViewModel, myInfoVieWModel)
                    5 -> MyInfoFlag(viewModel, stateViewModel, myInfoVieWModel)
                }
            }
        }
    }
}

@Composable
fun MyInfoStatus(
    mainViewModel: DhMainViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val status by myInfoViewModel.status.collectAsState()
    var characterName by remember { mutableStateOf("") }
    var characterJob by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            characterName = "${it.characterName} (Lv.${it.level})"
            characterJob = "${it.jobName} [${it.jobGrowName}]"

            myInfoViewModel.getStatus(it.serverId, it.characterId)
        }
    }

    Column(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = characterName,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = characterJob,
            color = Color.White
        )
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = "명성 : ${status.status?.find { it.name == "모험가 명성" }?.value}",
            color = Color.White
        )
        Separator()

        LazyVerticalGrid(
            columns = GridCells.Fixed(3)
        ) {
            items(items = status.status ?: return@LazyVerticalGrid) { status: Status ->
                StatusCard(status)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MyInfoEquipment(
    mainViewModel: DhMainViewModel,
    stateViewModel: DhStateViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val equipment by myInfoViewModel.equipment.collectAsState()
    var itemIndex by remember { mutableStateOf(0) }
    var isShowingOptionDialog by remember { mutableStateOf(false) }
    val itemInfo by mainViewModel.itemInfo.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isShowingBottomSheet by stateViewModel.isShowingBottomSheet.collectAsState()
    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val isShowingSetItemInfoBottomSheet by stateViewModel.isShowingSetItemInfoBottomSheet.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.getEquipment(it.serverId, it.characterId)
        }
    }

    Column(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        equipment.setItemInfo?.get(0)?.let { setItem ->
            SetItemInfoCard(
                stateViewModel, setItem
            )
            Separator()
        }

        equipment.equipment?.let {
            LazyColumn {
                items(items = equipment.equipment ?: return@LazyColumn) { item ->
                    ItemCard(ItemRows(item)) {
                        stateViewModel.setIsShowingBottomSheet(true)
                        itemIndex = equipment.equipment?.indexOf(item) ?: 0
                    }
                }
            }
        } ?: DhCircularProgress()
    }


    if(isShowingBottomSheet) {
        DhModalBottomSheet(
            sheetState = sheetState,
            stateViewModel = stateViewModel,
            textList = listOf("아이템 정보 보기", "융합석/마법부여 정보 보기"),
            callbackList = listOf(
                {
                    stateViewModel.setIsShowingBottomSheet(false)
                    stateViewModel.setIsShowingItemInfoDialog(true)
                    mainViewModel.getItemInfo(equipment.equipment?.get(itemIndex)?.itemId ?: return@listOf)
                },
                {
                    stateViewModel.setIsShowingBottomSheet(false)
                    isShowingOptionDialog = true
                    mainViewModel.getItemInfo(equipment.equipment?.get(itemIndex)?.upgradeInfo?.itemId ?: return@listOf)
                }
            )
        )
    }

    if(isShowingOptionDialog) {
        AlertDialog(
            onDismissRequest = { isShowingOptionDialog = false },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isShowingOptionDialog = false
                    }
                ) {
                    Text(
                        text = "닫기",
                        color = Color.White
                    )
                }
            },
            text = {
                Column {
                    equipment.equipment?.get(itemIndex)?.upgradeInfo?.let {
                        Text(
                            text = "융합석",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(15f, TextUnitType.Sp)
                        )
                        ItemCard(ItemRows(itemInfo))
                        LazyColumn {
                            items(items = itemInfo.fusionOption?.options ?: return@LazyColumn) {
                                Text(
                                    text = it.explain,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    equipment.equipment?.get(itemIndex)?.enchant?.let {
                        Text(
                            text = "마법부여",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(15f, TextUnitType.Sp)
                        )
                        Spacer(Modifier.height(5.dp))
                        LazyColumn {
                            items(items = it.status) {
                                Text(
                                    text = "${it.name} + ${it.value}",
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        )
    }

    if(isShowingItemInfoDialog) {
        ItemInfoDialog(itemInfo, stateViewModel)
    }

    if(isShowingSetItemInfoBottomSheet) {
        DhSetItemInfoBottomSheet(sheetState, stateViewModel, equipment.setItemInfo?.get(0)?.active ?: return)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun MyInfoAvatar(
    mainViewModel: DhMainViewModel,
    stateViewModel: DhStateViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val avatar by myInfoViewModel.avatar.collectAsState()
    val isShowingBottomSheet by stateViewModel.isShowingBottomSheet.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    var itemIndex by remember { mutableStateOf(0) }
    val cloneItemInfo by mainViewModel.itemInfo.collectAsState()
    val context = LocalContext.current
    var isShowingEmblemsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.getAvatar(it.serverId, it.characterId)
        }
    }

    LazyColumn(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        items(items = avatar.avatar ?: return@LazyColumn) { item ->
            ItemCard(ItemRows(item)) {
                stateViewModel.setIsShowingBottomSheet(true)
                itemIndex = avatar.avatar?.indexOf(item) ?: 0
            }
        }
    }

    if(isShowingBottomSheet) {
        DhModalBottomSheet(
            sheetState = sheetState,
            stateViewModel = stateViewModel,
            listOf("클론 아바타 정보", "엠블렘 정보"),
            listOf(
                {
                    // "클론 아바타 정보"
                    stateViewModel.setIsShowingBottomSheet(false)
                    mainViewModel.getItemInfo(avatar.avatar?.get(itemIndex)?.clone?.itemId ?: run {
                        Toast.makeText(context, "클론 아바타가 없습니다.", Toast.LENGTH_SHORT).show()
                        return@listOf
                    })
                    stateViewModel.setIsShowingItemInfoDialog(true)
                },
                {
                    // "엠블렘 정보"
                    stateViewModel.setIsShowingBottomSheet(false)
                    if(avatar.avatar?.get(itemIndex)?.emblems?.isNotEmpty() == true) {
                        isShowingEmblemsDialog = true
                    } else {
                        Toast.makeText(context, "장착된 엠블렘이 없습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        )
    }

    if(isShowingItemInfoDialog) {
        ItemInfoDialog(
            dto = cloneItemInfo,
            stateViewModel
        )
    }

    if(isShowingEmblemsDialog) {
        AlertDialog(
            onDismissRequest = { isShowingEmblemsDialog = false },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        isShowingEmblemsDialog = false
                    }
                ) {
                    Text(text = "닫기", color = Color.White)
                }
            },
            text = {
                LazyColumn {
                    items(items = avatar.avatar?.get(itemIndex)?.emblems ?: return@LazyColumn) { emblem ->
                        Spacer(Modifier.height(15.dp))
                        Column {
                            Text(
                                text = "${emblem.slotNo}번째 슬롯 [${emblem.slotColor}]",
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(5.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                GlideSubcomposition(
                                    model = String.format(NetworkConstants.ITEM_URL, emblem.itemId),
                                    modifier = Modifier.size(55.dp)
                                ) {
                                    when (state) {
                                        RequestState.Loading -> CircularProgressIndicator()
                                        RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                                        is RequestState.Success -> Image(painter = painter, contentDescription = null)
                                    }
                                }
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = emblem.itemName,
                                    color = Color(emblem.itemRarity.convertRarityColor()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = TextUnit(15f, TextUnitType.Sp)
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun MyInfoBuffEquipment(
    mainViewModel: DhMainViewModel,
    stateViewModel: DhStateViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    fun getDesc(option: Option) : String {
        val values = option.values
        var result = option.desc
        (1 .. values.size).forEach {
            result = result.replace("{value$it}", values[it-1])
        }
        return result.also { Log.d("getDesc() result > $it") }
    }
    val buffEquipment by myInfoViewModel.buffSkillEquip.collectAsState()
    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val itemInfo by mainViewModel.itemInfo.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.getBuffSkillEquip(it.serverId, it.characterId)
        }
    }
    Column {
        val skillInfo = buffEquipment.skill?.buff?.skillInfo
        Text(
            text = "${skillInfo?.name} Lv.${skillInfo?.option?.level}" ?: return,
            color = Color.White,
            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold
        )
        Text(text = getDesc(skillInfo?.option ?: return), color = Color.White)
        LazyColumn(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            items(items = buffEquipment.skill?.buff?.equipment ?: return@LazyColumn) {
                ItemCard(ItemRows(it)) {
                    stateViewModel.setIsShowingItemInfoDialog(true)
                    mainViewModel.getItemInfo(it)
                }
            }
        }
    }

    if(isShowingItemInfoDialog) {
        ItemInfoDialog(
            dto = itemInfo,
            stateViewModel = stateViewModel
        )
    }
}

@Composable
fun MyInfoFlag(
    mainViewModel: DhMainViewModel,
    stateViewModel: DhStateViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val flag by myInfoViewModel.flag.collectAsState()
    val itemInfo by mainViewModel.itemInfo.collectAsState()
    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val itemCardClickLambda = { itemId: String ->
        mainViewModel.getItemInfo(itemId)
        stateViewModel.setIsShowingItemInfoDialog(true)
    }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.getFlag(it.serverId, it.characterId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ItemCard(ItemRows(flag.flag ?: return), itemCardClickLambda)
        LazyColumn(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            items(items = flag.flag?.gems ?: return@LazyColumn) {
                ItemCard(ItemRows(it), itemCardClickLambda)
            }
        }
    }

    if(isShowingItemInfoDialog) {
        ItemInfoDialog(
            itemInfo,
            stateViewModel
        )
    }
}

@Composable
fun MyInfoCreature(
    mainViewModel: DhMainViewModel,
    stateViewModel: DhStateViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val creature by myInfoViewModel.creature.collectAsState()
    val itemInfo by mainViewModel.itemInfo.collectAsState()
    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val itemCardClickLambda = { itemId: String ->
        mainViewModel.getItemInfo(itemId)
        stateViewModel.setIsShowingItemInfoDialog(true)
    }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.getCreature(it.serverId, it.characterId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ItemCard(ItemRows(creature.creature ?: return), itemCardClickLambda)
        LazyColumn(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            items(items = creature.creature?.artifact ?: return@LazyColumn) {
                ItemCard(ItemRows(it), itemCardClickLambda)
            }
        }
    }

    if(isShowingItemInfoDialog) {
        ItemInfoDialog(
            dto = itemInfo,
            stateViewModel = stateViewModel
        )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun SetItemInfoCard(
    stateViewModel: DhStateViewModel,
    setItem: SetItemInfo
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .clickable {
                stateViewModel.setIsShowingSetItemInfoBottomSheet(true)
            },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideSubcomposition(
            model = String.format(NetworkConstants.ITEM_URL, setItem.setItemId),
            modifier = Modifier.size(35.dp)
        ) {
            when (state) {
                RequestState.Loading -> CircularProgressIndicator()
                RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                is RequestState.Success -> Image(painter = painter, contentDescription = null)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = setItem.setItemName,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = setItem.setItemRarityName,
                color = Color.White
            )
            Text(
                text = "${setItem.active.setPoint.current} / ${setItem.active.setPoint.max}",
                color = Color.White
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhSetItemInfoBottomSheet(
    sheetState: SheetState,
    stateViewModel: DhStateViewModel,
    active: Active
) {
    ModalBottomSheet(
        onDismissRequest = { stateViewModel.setIsShowingSetItemInfoBottomSheet(false) },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(16.dp)
                .verticalScroll(state = rememberScrollState())
        ) {
            if(!active.explain.isNullOrEmpty()) {
                Text(
                    text = active.explain,
                    color = Color.White
                )
            }
            if(!active.buffExplain.isNullOrEmpty()) {
                Text(
                    text = active.buffExplain,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            active.status.forEach {
                Text(
                    text = "${it.name} - ${it.value}",
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun StatusCard(status: Status) {
    Column(
        modifier = Modifier
            .height(120.dp)
            .padding(5.dp)
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(size = 10.dp)
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = status.name,
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(5.dp))
        Text(
            text = status.value,
            color = Color.White
        )
    }
}

@Composable
fun Separator() {
    Spacer(
        modifier = Modifier.fillMaxWidth()
            .height(1.dp)
            .border(
                width = 1.dp,
                color = Color.White
            )
    )
}