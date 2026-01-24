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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
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
import com.jeepchief.dh.core.network.dto.Avatar
import com.jeepchief.dh.core.network.dto.Equipment
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.network.dto.ItemsDTO
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
import com.jeepchief.dh.features.main.navigation.Divider
import com.jeepchief.dh.features.main.navigation.ItemCard
import com.jeepchief.dh.features.main.navigation.ItemInfoDialog
import com.jeepchief.dh.ui.theme.DefaultBackColor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
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
                context.getString(R.string.tab_creature_and_gem),
                context.getString(R.string.tab_mist)
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
                    4 -> MyInfoCreatureAndFlag(viewModel, stateViewModel, myInfoVieWModel)
                    5 -> MyInfoMistAssimilation(viewModel, myInfoVieWModel)
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
    var characterGuild by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            characterName = "${it.characterName} (Lv.${it.level})"
            characterJob = "${it.jobName} [${it.jobGrowName}]"

            val guildString = if(it.guildName?.isNotEmpty() == true) "[${it.guildName}] 길드" else "길드 없음"
            val adventureString = if(it.adventureName.isNotEmpty() == true) "[${it.adventureName}] 모험단" else "모험단 없음"
            characterGuild = "$guildString | $adventureString"

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
        Text(
            modifier = Modifier.padding(bottom = 5.dp),
            text = characterGuild,
            color = Color.White
        )
        Divider()

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
    val itemInfo by mainViewModel.itemInfo.collectAsState()
    val upgradeItemInfo by myInfoViewModel.upgradeItemInfo.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val isShowingBottomSheet by stateViewModel.isShowingBottomSheet.collectAsState()
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
            Divider()
        }

        equipment.equipment?.let { equipmentList ->
            LazyColumn {
                items(items = equipmentList) { item ->
                    EquipmentCard(ItemRows(item)) {
                        stateViewModel.setIsShowingBottomSheet(true)
                        itemIndex = equipmentList.indexOf(item)
                        mainViewModel.getItemInfo(item.itemId)
                        myInfoViewModel.getUpgradeItemInfo(item.upgradeInfo?.itemId ?: return@EquipmentCard)
                    }
                }
            }
        } ?: DhCircularProgress()
    }

    if(isShowingSetItemInfoBottomSheet) {
        DhSetItemInfoBottomSheet(sheetState, stateViewModel, equipment.setItemInfo?.get(0)?.active ?: return)
    }

    if(isShowingBottomSheet) {
        EquipmentInfoBottomSheet(sheetState, equipment.equipment?.get(itemIndex) ?: return, itemInfo, upgradeItemInfo) {
            stateViewModel.setIsShowingBottomSheet(false)
        }
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
    var itemIndex by remember { mutableStateOf(0) }
    val context = LocalContext.current
    val emblemInfos by myInfoViewModel.emblems.collectAsState()
    val isShowingEmblemInfoDialog by myInfoViewModel.emblemsInfoTrigger.collectAsState()
    var isShowingEmptyEmblemMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.getAvatar(it.serverId, it.characterId)
        }
    }


    if(avatar.avatar.isNullOrEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "장착된 아바타가 없습니다",
                color = Color.White
            )
        }
    } else {
        val avatars = avatar.avatar ?: emptyList()

        LazyColumn(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            itemsIndexed(items = avatars) { index, item ->
                AvatarCard(item) {
                    // "엠블렘 정보"
                    if(item.emblems.isNullOrEmpty()) {
                        isShowingEmptyEmblemMessage = true
                    } else {
                        itemIndex = index
                        myInfoViewModel.getEmblems(
                            item.emblems.map { it.itemId }
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(isShowingEmptyEmblemMessage) {
        if(isShowingEmptyEmblemMessage) {
            Toast.makeText(context, "장착된 엠블렘이 없습니다.", Toast.LENGTH_SHORT).show()
            isShowingEmptyEmblemMessage = false
        }
    }

    if(isShowingEmblemInfoDialog) {
        AlertDialog(
            onDismissRequest = { myInfoViewModel.initEmblemTrigger() },
            properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
            confirmButton = {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        myInfoViewModel.initEmblemTrigger()
                    }
                ) {
                    Text(text = "닫기", color = Color.White)
                }
            },
            text = {
                LazyColumn {
                    itemsIndexed(items = avatar.avatar?.get(itemIndex)?.emblems ?: return@LazyColumn) { index, emblem ->
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
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    when (state) {
                                        RequestState.Loading -> CircularProgressIndicator()
                                        RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                                        is RequestState.Success -> Image(painter = painter, contentDescription = null)
                                    }
                                }
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = emblem.itemName,
                                        color = Color(emblem.itemRarity.convertRarityColor()),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = TextUnit(15f, TextUnitType.Sp)
                                    )

                                    emblemInfos[index].itemStatus?.onEach { status ->
                                        Spacer(Modifier.height(5.dp))
                                        Text(
                                            text = "${status.name} + ${status.value}",
                                            color = Color.White
                                        )
                                    } ?: DhCircularProgress()
                                }
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
    val buffAvatar by myInfoViewModel.buffSkillAvatar.collectAsState()
    val buffCreature by myInfoViewModel.buffSKillCreature.collectAsState()

    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val itemInfo by mainViewModel.itemInfo.collectAsState()

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.run {
                getBuffSkillEquip(it.serverId, it.characterId)
                getBuffSkillAvatar(it.serverId, it.characterId)
                getBuffSkillCreature(it.serverId, it.characterId)
            }
        }
    }

    buffEquipment.skill?.buff?.let { buff ->
        Column {
            LazyColumn(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                item {
                    val skillInfo = buff.skillInfo
                    Text(
                        text = "${skillInfo.name} Lv.${skillInfo.option.level}",
                        color = Color.White,
                        fontSize = TextUnit(20f, TextUnitType.Sp),
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = getDesc(skillInfo.option), color = Color.White)
                }
                items(items = buff.equipment ?: return@LazyColumn) {
//                    ItemCard(ItemRows(it)) {
//                        stateViewModel.setIsShowingItemInfoDialog(true)
//                        mainViewModel.getItemInfo(it)
//                    }

                    EquipmentCard(ItemRows(it)) {
                        stateViewModel.setIsShowingItemInfoDialog(true)
                        mainViewModel.getItemInfo(it)
                    }
                }

                buffAvatar.skill?.buff?.let { buff ->
                    items(items = buff.avatar ?: return@LazyColumn) {
                        AvatarCard(it) { mainViewModel.getItemInfo(it) }
                    }
                } ?: item { DhCircularProgress() }

                buffCreature.skill?.buff?.let { buff ->
                    items(items = buff.creature ?: return@LazyColumn) {
                        EquipmentCard(ItemRows(it)) { mainViewModel.getItemInfo(it) }
                    }
                } ?: item { DhCircularProgress() }
            }
        }
    } ?: DhCircularProgress()


    if(isShowingItemInfoDialog) {
        ItemInfoDialog(
            dto = itemInfo,
            stateViewModel = stateViewModel
        )
    }
}

@Composable
fun MyInfoCreatureAndFlag(
    mainViewModel: DhMainViewModel,
    stateViewModel: DhStateViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val creature by myInfoViewModel.creature.collectAsState()
    val flag by myInfoViewModel.flag.collectAsState()
    val itemInfo by mainViewModel.itemInfo.collectAsState()

    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val itemCardClickLambda = { itemId: String ->
        mainViewModel.getItemInfo(itemId)
        stateViewModel.setIsShowingItemInfoDialog(true)
    }

    LaunchedEffect(Unit) {
        mainViewModel.nowCharacterInfo.collectLatest {
            myInfoViewModel.run {
                getCreature(it.serverId, it.characterId)
                getFlag(it.serverId, it.characterId)
            }
        }
    }

    creature.creature?.let { mCreature ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                item {
                    EquipmentCard(ItemRows(mCreature), itemCardClickLambda)
                }
                items(items = mCreature.artifact) {
                    Row {
                        Spacer(Modifier.width(10.dp))
                        ItemCard(ItemRows(it), itemCardClickLambda)
                    }
                }
                flag.flag?.let {
                    item {
                        EquipmentCard(ItemRows(it), itemCardClickLambda)
                    }
                    items(it.gems) {
                        Row {
                            Spacer(Modifier.width(10.dp))
                            ItemCard(ItemRows(it), itemCardClickLambda)
                        }
                    }
                }
            }
        }
    } ?: DhCircularProgress()

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
        modifier = Modifier
            .fillMaxWidth()
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
                RequestState.Loading -> DhCircularProgress()
                RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                is RequestState.Success -> Image(painter = painter, contentDescription = null)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = setItem.setItemName,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            setItem.setItemRarityName?.let {
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
            modifier = Modifier
                .fillMaxSize()
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentInfoBottomSheet(
    sheetState: SheetState,
    equipment: Equipment,
    dto: ItemsDTO,
    upgrade: ItemsDTO,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .nestedScroll(rememberNestedScrollInteropConnection())
        ) {
            item {
                ItemCard(ItemRows(dto))
                dto.jobs?.let { jobs ->
                    dto.itemStatus ?: return@let

                    val jobString = StringBuilder().also { it.append(jobs[0].jobName) }
                    for(i in 1 until  jobs.size) {
                        jobString.append("/ ${jobs[i].jobName}")
                    }
                    Text(
                        text = jobString.append(" 사용가능").toString(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(5.dp))
                }
            }
            items(items = dto.itemStatus ?: listOf()) {
                Text(
                    text = "${it.name} + ${it.value}",
                    color = Color.White
                )
            }
            item {
                equipment.tune?.let { info ->
                    if(info[0].setPoint == 0) return@let

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "세트포인트 + ${info[0].setPoint}",
                        color = Color.White
                    )
                }

                if(dto.itemExplain.isNotEmpty()) {
                    Text(
                        text = dto.itemExplain,
                        color = Color.White
                    )
                }

                dto.fixedOption?.let {
                    Text(
                        text = it.explain,
                        color = Color.White
                    )
                }

                dto.fusionOption?.let {
                    it.options.forEach { option ->
                        Text(
                            text = option.explain,
                            color = Color.White
                        )
                    }
                }

                if(dto.itemFlavorText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = dto.itemFlavorText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }

                // TODO: itemBuff 필드 구현 필요
            }

            equipment.upgradeInfo?.let {
                item {
                    Spacer(Modifier.height(10.dp))
                    Divider()
                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "융합석",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(15f, TextUnitType.Sp)
                    )

                    ItemCard(ItemRows(upgrade))
                    Text(
                        text = upgrade.fusionOption?.options?.get(0)?.explain ?: "",
                        color = Color.White
                    )
                }
            }

            // 마법부여
            equipment.enchant?.let { enchant ->
                item {
                    Spacer(Modifier.height(10.dp))
                    Divider()
                    Spacer(Modifier.height(5.dp))
                    Text(
                        text = "마법부여",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(15f, TextUnitType.Sp)
                    )
                    Spacer(Modifier.height(5.dp))
                }
                items(items = enchant.status) {
                    Text(
                        text = "${it.name} + ${it.value}",
                        color = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
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


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun EquipmentCard(row: ItemRows, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable(onClick = { onClick(row.itemId) })
            .border(1.dp, Color.White, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideSubcomposition(
                model = String.format(NetworkConstants.ITEM_URL, row.itemId),
                modifier = Modifier.size(35.dp)
            ) {
                when (state) {
                    RequestState.Loading -> DhCircularProgress()
                    RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                    is RequestState.Success -> Image(painter = painter, contentDescription = null)
                }
            }

            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                val itemDisplayText =
                    if(row.reinforce > 0)  "(+${row.reinforce}) ${row.itemName}"
                    else                    "${row.itemName}"

                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = itemDisplayText,
                    color = Color(row.itemRarity.convertRarityColor()),
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
                if(row.itemType.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "${row.itemType}-${row.itemTypeDetail}",
                            color = Color.White,
                            fontSize = TextUnit(12f, TextUnitType.Sp)
                        )

                        if(row.tuneLevel > 0) {
                            Text(
                                text = "${row.tuneLevel}조율",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        row.upgradeInfo?.let { info ->
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideSubcomposition(
                    model = String.format(NetworkConstants.ITEM_URL, info.itemId),
                    modifier = Modifier.size(35.dp)
                ) {
                    when (state) {
                        RequestState.Loading -> DhCircularProgress()
                        RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                        is RequestState.Success -> Image(painter = painter, contentDescription = null)
                    }
                }

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = info.itemName,
                    color = Color(info.itemRarity.convertRarityColor()),
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AvatarCard(avatar: Avatar, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .padding(5.dp)
            .clickable(onClick = { onClick(avatar.itemId) })
            .border(1.dp, Color.White, RoundedCornerShape(10.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideSubcomposition(
                model = String.format(NetworkConstants.ITEM_URL, avatar.itemId),
                modifier = Modifier.size(35.dp)
            ) {
                when (state) {
                    RequestState.Loading -> DhCircularProgress()
                    RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                    is RequestState.Success -> Image(painter = painter, contentDescription = null)
                }
            }

            Column(
                modifier = Modifier.padding(start = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "[${avatar.slotName}]  ",
                        color = Color.White,
                        fontSize = TextUnit(12f, TextUnitType.Sp)
                    )
                    Text(
                        text = avatar.itemName,
                        color = Color(avatar.itemRarity.convertRarityColor()),
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(14f, TextUnitType.Sp)
                    )
                }
                avatar.optionAbility?.let {
                    Text(
//                        modifier = Modifier.padding(start = 5.dp),
                        text = it,
                        color = Color.White,
                        fontSize = TextUnit(12f, TextUnitType.Sp)
                    )
                }

            }
        }

        if(avatar.clone.itemId != null) {
            Divider()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                GlideSubcomposition(
                    model = String.format(NetworkConstants.ITEM_URL, avatar.clone.itemId),
                    modifier = Modifier.size(35.dp)
                ) {
                    when (state) {
                        RequestState.Loading -> DhCircularProgress()
                        RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                        is RequestState.Success -> Image(painter = painter, contentDescription = null)
                    }
                }

                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = avatar.clone.itemName ?: "",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = TextUnit(14f, TextUnitType.Sp)
                )
            }
        }
    }
}

@Composable
fun MyInfoMistAssimilation(
    mainViewModel: DhMainViewModel,
    myInfoViewModel: DhMyInfoViewModel
) {
    val mist by myInfoViewModel.mistAssimilation.collectAsState()

    LaunchedEffect(Unit) {
        val info = mainViewModel.nowCharacterInfo.first()
        myInfoViewModel.getMistAssimilation(
            info.serverId, info.characterId
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        mist.mistAssimilation?.let { mist ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(20.dp)
                ) {
                    Image(
                        painterResource(R.drawable.mist),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Text(
                        text = mist.level.toString(),
                        color = Color.White
                    )
                }
                Column {
                    Text(
                        text = "레벨: ${mist.level}",
                        color = Color.White
                    )
                    Text(
                        text = "경험치: ${mist.expRate}",
                        color = Color.White
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(3)
            ) {
                items(items = mist.status) {
                    StatusCard(it)
                }
            }
        } ?: run {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "안개 융화 단계가 존재하지 않습니다.",
                    color = Color.White
                )
            }
        }
    }
}