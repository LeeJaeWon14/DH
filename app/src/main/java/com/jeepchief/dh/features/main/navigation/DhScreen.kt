package com.jeepchief.dh.features.main.navigation

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.rememberNestedScrollInteropConnection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.google.gson.Gson
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.network.dto.AuctionRows
import com.jeepchief.dh.core.network.dto.Avatar
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.network.dto.Option
import com.jeepchief.dh.core.network.dto.Status
import com.jeepchief.dh.core.network.dto.TimeLineRows
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.Pref
import com.jeepchief.dh.core.util.convertRarityColor
import com.jeepchief.dh.core.util.makeComma
import com.jeepchief.dh.core.util.toWordType
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.activity.CharacterCard
import com.jeepchief.dh.features.main.activity.MainActivity
import com.jeepchief.dh.features.main.activity.ShowCharacterSearchDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class DhScreen(val route: String, val drawableId: Int, val stringId: Int) {
    Main("Main", -1, -1),
    MyInfo("MyInfo", R.drawable.ic_baseline_people_24, R.string.button_name_my_info),
    ItemSearch("ItemSearch", R.drawable.ic_baseline_find_in_page_24, R.string.button_name_search_items),
    Auction("Auction", R.drawable.ic_baseline_shopping_cart_checkout_24, R.string.button_name_auction),
    Character("Character", R.drawable.ic_baseline_autorenew_24, R.string.button_name_change_character),
    FameSearch("Fame", R.drawable.ic_baseline_token_24, R.string.button_name_fame),
    TimeLIne("TimeLine", R.drawable.ic_baseline_access_time_24, R.string.button_name_timeline)
}


//navController.navigate(route.route) {
//    popUpTo(prevRoute.route) { inclusive = true }
//}

//@Preview(showSystemUi = false)
@Composable
fun MainScreen(navHostController: NavHostController) {
    val backgroundRes = remember {
        when(Random.nextInt(6)) {
            0 -> R.drawable.main_background
            1 -> R.drawable.main_background_2
            2 -> R.drawable.main_background_3
            4 -> R.drawable.main_backgroujnd_4
            5 -> R.drawable.main_background_5
            else -> R.drawable.main_background_6
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(backgroundRes),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            MainScreenGrid(navHostController, arrayOf(DhScreen.MyInfo, DhScreen.ItemSearch))
            Spacer(modifier = Modifier.height(10.dp))
            MainScreenGrid(navHostController, arrayOf(DhScreen.Auction, DhScreen.Character))
            Spacer(modifier = Modifier.height(10.dp))
            MainScreenGrid(navHostController, arrayOf(DhScreen.FameSearch, DhScreen.TimeLIne))
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun MyInfoScreen(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    BaseScreen(stateViewModel, false) {
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
                        modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                    ) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = TextUnit(18f, TextUnitType.Sp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
            ) { page ->
                when (page) {
                    0 -> MyInfoStatus(viewModel)
                    1 -> MyInfoEquipment(viewModel)
                    2 -> MyInfoAvatar(viewModel, stateViewModel)
                    3 -> MyInfoBuffEquipment(viewModel)
                    4 -> MyInfoCreature(viewModel)
                    5 -> MyInfoFlag(viewModel, stateViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchScreen(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    BaseScreen(stateViewModel) {
        val context = LocalContext.current
        var searchChanged by remember { mutableStateOf("") }
        val itemSearch by viewModel.itemSearch.collectAsState()
        val itemInfo by viewModel.itemInfo.collectAsState()
        val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
        val isShowingSearchSettingDialog by stateViewModel.isShowingSearchSettingDialog.collectAsState()
        var mWordType by remember { mutableStateOf(context.getString(R.string.text_word_type_front)) }
        var mRarity by remember { mutableStateOf("") }
        var isHideKeyboard by remember { mutableStateOf(false) }

        val searchAction = {
            if(searchChanged.isNotEmpty()) {
                isHideKeyboard = true
                viewModel.getSearchItems(
                    searchChanged,
                    mWordType.toWordType(),
                    mRarity
                )
            }
        }

        if(isHideKeyboard) {
            HideKeyboard()
            isHideKeyboard = false
        }

        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchChanged,
                onValueChange = { searchChanged = it },
                singleLine = true,
                textStyle = TextStyle(color = Color.White),
                label = {
                    Text(text = stringResource(R.string.text_input_item_name_hint), color = Color.White)
                },
                trailingIcon = {
                    ItemSearchField(
                        modifier = Modifier.padding(end = 10.dp),
                        searchClickCallback = searchAction,
                        settingClickCallback = { stateViewModel.setIsShowingSearchSettingDialog(true) }
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { searchAction() }
                )
            )

            itemSearch.rows?.let { rows ->
                if(rows.isEmpty()) {
                    LaunchedEffect(Unit) {
                        Toast.makeText(context, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show()
                        return@LaunchedEffect
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(rows) { row ->
                        ItemCard(row) { itemId ->
                            viewModel.getItemInfo(itemId)
                            stateViewModel.setIsShowingItemInfoDialog(true)
                        }
                    }
                }
            }
        }

        if(isShowingItemInfoDialog) {
            LaunchedEffect(Unit) { delay(500) }
            ItemInfoDialog(itemInfo, stateViewModel)
        }

        if(isShowingSearchSettingDialog) {
            SearchSettingDialog(viewModel, stateViewModel) { wordType, rarity ->
                mWordType = wordType
                mRarity = rarity
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AuctionScreen(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    BaseScreen(stateViewModel) {
        var textChanged by remember { mutableStateOf("") }
        val isShowingAuctionResultDialog by stateViewModel.isShowingAuctionResultDialog.collectAsState()
        val isShowingAuctionSettingDialog by stateViewModel.isShowingAuctionSettingDialog.collectAsState()
        val auction by viewModel.auction.collectAsState()
        var index by remember { mutableStateOf(0) }
        var isHideKeyboard by remember { mutableStateOf(false) }

        val searchAction = {
            if(textChanged.isNotEmpty()) {
                isHideKeyboard = true
                viewModel.getAuction(
                    sort = stateViewModel.priceSort.value,
                    itemName = textChanged
                )
            }
        }

        if(isHideKeyboard) {
            HideKeyboard()
            isHideKeyboard = false
        }

        Column {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textChanged,
                onValueChange = { textChanged = it },
                singleLine = true,
                label = {
                    Text(text = stringResource(R.string.text_input_item_name_hint), color = Color.White)
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { searchAction() }
                ),
                trailingIcon = {
                    ItemSearchField(
                        modifier = Modifier.padding(end = 10.dp),
                        searchClickCallback = searchAction,
                        settingClickCallback = {
                            stateViewModel.setIsShowingAuctionSettingDialog(true)
                        }
                    )
                }
            )

            auction.rows?.let { rows ->
                LazyColumn {
                    items(items = rows) { row ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, bottom = 5.dp)
                                .clickable(onClick = {
                                    index = rows.indexOf(row)
                                    stateViewModel.setIsShowingAuctionResultDialog(true)
                                }),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                model = String.format(NetworkConstants.ITEM_URL, row.itemId),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(55.dp)
                            )

                            Spacer(Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${row.itemName}\r\n(Lv. ${row.itemAvailableLevel})",
                                    color = Color(row.itemRarity.convertRarityColor()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = TextUnit(15f, TextUnitType.Sp)
                                )
//                            if(row.itemType.isNotEmpty()) {
//                                Text(
//                                    text = "${row.itemType}-${row.itemTypeDetail}",
//                                    color = Color.White
//                                )
//                            }

                                Text(
                                    text = row.currentPrice.toString().makeComma(),
                                    color = Color.White
                                )

                            }
                        }
                    }
                }
            }
        }

        if(isShowingAuctionResultDialog) {
            AuctionInfoDialog(auction.rows?.get(index) ?: return@BaseScreen, stateViewModel)
        }

        if(isShowingAuctionSettingDialog) {
            AuctionSettingDialog(stateViewModel)
        }
    }
}

@Composable
fun CharacterScreen(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    BaseScreen(stateViewModel) {
        val isShowingCharacterSearchDialog by stateViewModel.isShowingCharacterSearchDialog.collectAsState()
        val isShowingCharacterRemoveDialog by stateViewModel.isShowingCharacterRemoveDialog.collectAsState()
        val characterList by viewModel.allCharacters.collectAsState()
        val context = LocalContext.current
        var deleteTarget by remember { mutableStateOf("") }

        if(isShowingCharacterSearchDialog) {
            ShowCharacterSearchDialog(
                viewModel, stateViewModel
            ) { row ->
                viewModel.insertCharacter(row)
            }
        }

        LazyColumn {
            items(items = characterList) {
                val row = it.toRow()
                CharacterCard(
                    character = row,
                    longClickCallback = {
                        deleteTarget = it
                        stateViewModel.setIsShowingCharacterRemoveDialog(true)
                    }
                ) {
                    Pref.setValue(Pref.CHARACTER_INFO, Gson().toJson(row))
                    context.startActivity(
                        Intent(context, MainActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    )
                }
            }
        }

        if(isShowingCharacterRemoveDialog) {
            AlertDialog(
                onDismissRequest = { stateViewModel.setIsShowingCharacterRemoveDialog(false) },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
                confirmButton = {
                    Button(onClick = {
                        viewModel.deleteCharacter(deleteTarget)
                        stateViewModel.setIsShowingCharacterRemoveDialog(false)
                    }) {
                        Text(text = "삭제", color = Color.White)
                    }
                },
                dismissButton = {
                    Button(onClick = {
                        stateViewModel.setIsShowingCharacterRemoveDialog(false)
                    }) {
                        Text(text = "취소", color = Color.White)
                    }
                },
                text = {
                    Text(text = "캐릭터를 삭제하시겠습니까?", color = Color.White)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FameScreen(viewModel: MainViewModel = hiltViewModel(), stateViewModel: DhStateViewModel = hiltViewModel()) {
    BaseScreen(stateViewModel) {
        var fameTextChanged by remember { mutableStateOf("") }
        var jobTextChanged by remember { mutableStateOf("") }
        var jobGrowTextChanged by remember { mutableStateOf("") }
        var jobGrowId by remember { mutableStateOf("") }
        var jobId by remember { mutableStateOf("") }
        val jobExpanded by stateViewModel.jobExpanded.collectAsState()
        val jobGrowExpanded by stateViewModel.jobGrowExpanded.collectAsState()
        val isShowingFameInfoDialog by stateViewModel.isShowingFameInfoDialog.collectAsState()
        val jobs by viewModel.jobs.collectAsState()
        val fame by viewModel.fame.collectAsState()
        var isHideKeyboard by remember { mutableStateOf(false) }
        val context = LocalContext.current

        fun searchAction() {
            isHideKeyboard = true
            val pFame = runCatching { fameTextChanged.toInt() }.getOrDefault(0)
            viewModel.getFame(pFame, jobId, jobGrowId)
        }

        LaunchedEffect(Unit) {
            viewModel.getJobs()
        }

        jobs.jobRows?.let { row ->
            Column {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = fameTextChanged,
                    onValueChange = { fameTextChanged = it },
                    label = { Text(text = "명성 입력", color = Color.White) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { searchAction() }
                    ),
                    trailingIcon = {
                        Image(
                            modifier = Modifier.clickable { searchAction() },
                            painter = painterResource(R.drawable.ic_baseline_search_24),
                            contentDescription = null
                        )
                    }
                )
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = jobExpanded,
                        onExpandedChange = { stateViewModel.setJobExpanded(false) },
                        modifier = Modifier.weight(0.5f)
                    ) {
                        OutlinedTextField(
                            value = jobTextChanged,
                            onValueChange = {  },
                            readOnly = true,
                            enabled = false,
                            label = { Text("직업선택", color = Color.White) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(jobExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .clickable {
                                    stateViewModel.setJobExpanded(!jobExpanded)
                                }
                        )

                        ExposedDropdownMenu(
                            expanded = jobExpanded,
                            onDismissRequest = { stateViewModel.setJobExpanded(false) },
                        ) {
                            row.sortedBy { it.jobName }.forEach { job ->
                                DropdownMenuItem(
                                    text = { Text(text = job.jobName, color = Color.White) },
                                    onClick = {
                                        jobTextChanged = job.jobName
                                        jobId = job.jobId
                                        stateViewModel.setJobExpanded(false)
                                    }
                                )
                            }
                        }
                    }

                    Spacer(Modifier.width(5.dp))
                    ExposedDropdownMenuBox(
                        expanded = jobGrowExpanded,
                        onExpandedChange = { stateViewModel.setJobGrowExpanded(false) },
                        modifier = Modifier.weight(0.5f)
                    ) {
                        OutlinedTextField(
                            value = jobGrowTextChanged,
                            onValueChange = {  },
                            readOnly = true,
                            enabled = false,
                            label = { Text("전직선택", color = Color.White) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(jobGrowExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .clickable {
                                    stateViewModel.setJobGrowExpanded(!jobGrowExpanded)
                                }
                        )

                        ExposedDropdownMenu(
                            expanded = jobGrowExpanded,
                            onDismissRequest = { stateViewModel.setJobGrowExpanded(false) },
                        ) {
                            row.sortedBy { it.jobName }.forEach { job ->
                                job.subRows.forEach { subRow ->
                                    DropdownMenuItem(
                                        text = { Text(text = subRow.jobGrowName, color = Color.White) },
                                        onClick = {
                                            jobGrowTextChanged = subRow.jobGrowName
                                            jobGrowId = subRow.jobGrowId
                                            stateViewModel.setJobGrowExpanded(false)
                                        }
                                    )
                                }

                            }
                        }
                    }
                }
                fame.rows?.let {
                    Spacer(Modifier.height(10.dp))
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(items = it) { row ->
                            CharacterCard(CharacterRows(row)) {
                                Pref.setValue(Pref.CHARACTER_INFO, Gson().toJson(row))
                                context.startActivity(
                                    Intent(context, MainActivity::class.java).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        } ?: CircularProgressIndicator(color = Color.White)

        if(isShowingFameInfoDialog) {
            AlertDialog(
                onDismissRequest = { stateViewModel.setIsShowingFameInfoDialog(false) },
                confirmButton = {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            stateViewModel.setIsShowingFameInfoDialog(false)
                        }) {
                        Text(text = "닫기", color = Color.White)
                    }
                },
                text = {
                    Text(
                        """
                            넥슨에서 제공하는 API에 의해 검색 가능한
                            명성 범위는 [(최대명성값 - 2000) ~ 최대명성값] 입니다.
                            최대명성값의 기본값은 현재 게임 내 가장 높은 명성 기준입니다.
                        """.trimIndent(),
                        color = Color.White
                    )
                }
            )
        }

        if(isHideKeyboard) {
            HideKeyboard()
            isHideKeyboard = false
        }
    }
}

@Composable
fun TimeLineScreen(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    BaseScreen(stateViewModel) {
        val timeLine by viewModel.timeLine.collectAsState()
        var isFirst = false
        val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
        val context = LocalContext.current

        LaunchedEffect(Unit) {
            viewModel.getTimeLine()
        }

        timeLine.timeline?.let {
        var prevDate = try {
            it.rows[0].date.split(" ")[0]
        } catch (e: Exception) {
            LaunchedEffect(Unit) {
                Toast.makeText(context, "최근 타임라인 기록이 없습니다.", Toast.LENGTH_SHORT).show()
                backDispatcher?.onBackPressed()
            }
            return@BaseScreen
        }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 10.dp, end = 10.dp)
            ) {
                items(it.rows) { row ->
                    if(!isFirst) {
                        isFirst = true
                        Text(
                            text = prevDate,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(15f, TextUnitType.Sp)
                        )
                    }
                    if(prevDate != it.rows[0].date.split(" ")[0]) {
                        Text(
                            text = it.rows[0].date.split(" ")[0],
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(15f, TextUnitType.Sp)
                        )
                    }

                    Spacer(Modifier.height(10.dp))
                    TimeLineCard(row)
                }
            }

        } ?: CircularProgressIndicator()
    }
}

@Composable
fun MainMenuButton(drawableId: Int, stringId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(130.dp)
            .alpha(0.8f)
//            .background(colorResource(R.color.back_color))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(drawableId),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = stringResource(stringId))
        }
    }
}

@Composable
fun MainScreenGrid(
    navHostController: NavHostController,
    screens: Array<DhScreen>
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(screens) { screen ->
            MainMenuButton(
                drawableId = screen.drawableId,
                stringId = screen.stringId
            ) {
                navHostController.navigate(screen.route)
            }

            if(screens.indexOf(screen) != screens.lastIndex) {
                Spacer(modifier = Modifier.width(10.dp))
            }
        }
    }
}

@Composable
fun TimeLineCard(row: TimeLineRows) {
    val descMap = getTimeLineDesc(LocalContext.current, row)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                Color(descMap["rarity"]?.toLong() ?: 0xFFFFFFFF)
            )
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
//            color = Color.White
        )
    }
}

fun getTimeLineDesc(context: Context, row: TimeLineRows) : Map<String, String> {
    val resultMap = mutableMapOf<String, String>()
    when(row.code) {
        101 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_101))
            resultMap.put("detail", "아라드에서 모험 시작")
        }
        102 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_102))
//            resultMap.put("detail", "아라드에서 모험 시작")
        }
        103 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_103))
            resultMap.put("detail", String.format(context.getString(R.string.timeline_job_grown), row.data.jobGrowName))
        }
        104 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_104))
            resultMap.put("detail", context.getString(R.string.timeline_max_level))
        }
        105 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_105))
        }
        201 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_201))
        }
        202 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_202))
        }
        203 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_203))
        }
        204 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_204))
        }
        205 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_205))
        }
        206 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_206))
        }
        207 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_207))
        }
        208 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_208))
        }
        209 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_209))
        }
        301 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_301))
        }
        401 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_401))
        }
        402 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_402))
        }
        403 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_403))
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
        404 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_404))
        }
        405 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_405))
        }
        406 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_406))
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_item_succession),
                    row.data.itemName?.plus(" (+${row.data.reinforce})")
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        501 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_501))
        }
        502 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_502))
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_legendary),
                    row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
        503 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_503))
        }
        504 -> {
            resultMap.put("desc", context.getString(R.string.timeline_code_504))
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
            resultMap.put("desc", context.getString(R.string.timeline_code_505))
            resultMap.put(
                "detail",
                String.format(
                    context.getString(R.string.timeline_get_item_dungeon),
                    row.data.channelName, row.data.channelNo, row.data.dungeonName, row.data.itemName
                )
            )
            resultMap.put("rarity", row.data.itemRarity?.convertRarityColor().toString())
        }
//        506 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_506)
//        }
//        507 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_507)
//        }
//        508 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_508)
//        }
//        509 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_509)
//        }
//        510 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_510)
//        }
//        511 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_511)
//        }
//        512 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_512)
//        }
//        513 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_513)
//        }
//        514 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_514)
//        }
//        515 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_515)
//        }
//        516 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_516)
//        }
//        517 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_517)
//        }
//        518 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_518)
//        }
//        519 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_519)
//        }
//        520 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_520)
//        }
//        601 -> {
//            tvTimelineDesc.text = itemView.context.getString(R.string.timeline_code_601)
//        }
//        602 -> {
//            getItemMessage(
//                this@apply,
//                itemView.context.getString(R.string.timeline_code_602),
//                String.format(
//                    itemView.context.getString(R.string.timeline_get_talisman),
//                    data.itemObtainInfo?.itemName, data.itemObtainInfo?.obtainName, data.itemName
//                ),
//                data.itemRarity
//            )
//        }

    }

    return resultMap
}

@Composable
fun BaseScreen(stateViewModel: DhStateViewModel, isUsePadding: Boolean = true, content: @Composable () -> Unit) {
    LaunchedEffect(true) {
        stateViewModel.setIsShowingAppBar(false)
    }
    DisposableEffect(Unit) {
        onDispose {
            stateViewModel.setIsShowingAppBar(true)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = if (isUsePadding) WindowInsets.statusBars.asPaddingValues()
                    .calculateTopPadding() else 0.dp,
                start = if (isUsePadding) 10.dp else 0.dp,
                end = if (isUsePadding) 10.dp else 0.dp
            )
    ) {
        Image(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f),
            painter = painterResource(R.drawable.dnf_icon),
            contentDescription = null
        )
        content()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCard(row: ItemRows, onClick: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .clickable(onClick = {
                onClick(row.itemId)
            }),
        verticalAlignment = Alignment.CenterVertically
    ) {
//        GlideImage(
//            model = String.format(NetworkConstants.ITEM_URL, row.itemId),
//            contentDescription = null,
//            contentScale = ContentScale.Fit,
//            modifier = Modifier.size(55.dp)
//        )

        GlideSubcomposition(
            model = String.format(NetworkConstants.ITEM_URL, row.itemId),
            modifier = Modifier.size(55.dp)
        ) {
            when (state) {
                RequestState.Loading -> CircularProgressIndicator()
                RequestState.Failure -> Image(painter = painterResource(R.drawable.dnf_icon), contentDescription = null)
                is RequestState.Success -> Image(painter = painter, contentDescription = null)
            }
        }

        Column(
            modifier = Modifier.padding(start = 10.dp)
        ) {
            Text(
                text = "${row.itemName}\r\n(Lv. ${row.itemAvailableLevel})",
                color = Color(row.itemRarity.convertRarityColor()),
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(15f, TextUnitType.Sp)
            )
            if(row.itemType.isNotEmpty()) {
                Text(
                    text = "${row.itemType}-${row.itemTypeDetail}",
                    color = Color.White
                )
            }

        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemCardWithSubSlot(avatar: Avatar, viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    val cloneItem by viewModel.itemInfo.collectAsState()
    val itemInfo by viewModel.itemInfo.collectAsState()
    val isShowingDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()

    LaunchedEffect(Unit) {
//        viewModel.getItemInfo(avatar.clone.itemId ?: return@LaunchedEffect)
    }
    ItemCard(ItemRows(avatar)) { itemId ->
//        viewModel.getItemInfo(itemId)
//        stateViewModel.setIsShowingItemInfoDialog(true)
    }

//    ItemCard(ItemRows(cloneItem)) { itemId ->
//        Log.d("""
//            itemID: ${cloneItem.itemId}
//            itemName: ${cloneItem.itemName}
//        """.trimIndent())
//        viewModel.getItemInfo(itemId)
//        stateViewModel.setIsShowingItemInfoDialog(true)
//    }

//    Row {
//        Spacer(Modifier.width(10.dp))
//        ItemCard(ItemRows(cloneItem)) { itemId ->
//            viewModel.getItemInfo(itemId)
//        }
//    }

    if(isShowingDialog) {
        ItemInfoDialog(
            itemInfo,
            stateViewModel
        )
    }
}

@Composable
fun SearchSettingDialog(viewModel: MainViewModel, stateViewModel: DhStateViewModel, resultCallback: (String, String) -> Unit) {
    val searchType = listOf(
        stringResource(R.string.text_word_type_front),
        stringResource(R.string.text_word_type_full),
        stringResource(R.string.text_word_type_match)
    )
    val checkedSearchType by stateViewModel.searchType.collectAsState()

    val rarityType = listOf(
        stringResource(R.string.text_rarity_all),
        stringResource(R.string.text_rarity_common),
        stringResource(R.string.text_rarity_uncommon),
        stringResource(R.string.text_rarity_rare),
        stringResource(R.string.text_rarity_unique),
        stringResource(R.string.text_rarity_legendary),
        stringResource(R.string.text_rarity_epic),
        stringResource(R.string.text_rarity_cron),
        stringResource(R.string.text_rarity_myth),
        stringResource(R.string.text_rarity_taecho)
    )
    val checkedRarityType by stateViewModel.rarityType.collectAsState()

    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingSearchSettingDialog(false) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                onClick = {
                    stateViewModel.setIsShowingSearchSettingDialog(false)
                    resultCallback(
                        checkedSearchType,
                        if(checkedRarityType == rarityType[0]) "" else checkedRarityType
                    )
                }
            ) {
                Text(text = stringResource(R.string.button_name_setting_confirm))
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth()
            ) {
                SettingRadioButton(stringResource(R.string.text_word_type), searchType, checkedSearchType) {
                    stateViewModel.setSearchType(it)
                }
                Spacer(modifier = Modifier.height(15.dp))
                SettingRadioButton(stringResource(R.string.text_rarity_grade), rarityType, checkedRarityType) {
                    stateViewModel.setRarityType(it)
                }
            }
        }
    )
}

@Composable
fun SettingRadioButton(title: String, list: List<String>, initChecked: String = list[0], checkedResult: (String) -> Unit) {
    Text(
        text = title,
        fontSize = TextUnit(20f, TextUnitType.Sp),
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    Column(
        modifier = Modifier.padding(start = 10.dp)
    ) {
        list.forEach { type ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = initChecked == type,
                    onClick = {
//                        checkedResult.value = type
                        checkedResult(type)
                    }
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = type, color = Color.White)
            }
        }
    }
}

@Composable
fun ItemInfoDialog(dto: ItemsDTO, stateViewModel: DhStateViewModel) {
    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingItemInfoDialog(false) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { stateViewModel.setIsShowingItemInfoDialog(false) }
            ) {
                Text(text = stringResource(R.string.button_name_close))
            }
        },
        text = {
            Column(
                modifier = Modifier.nestedScroll(rememberNestedScrollInteropConnection())
            ) {
                ItemCard(ItemRows(dto)) {  }

                LazyColumn {
                    items(items = dto.itemStatus ?: return@LazyColumn) {
                        Text(
                            text = "${it.name} + ${it.value}",
                            color = Color.White
                        )
                    }
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

                if(dto.itemFlavorText.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = dto.itemFlavorText,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    )
}

@Composable
fun MyInfoStatus(viewModel: MainViewModel) {
    val status by viewModel.status.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getStatus()
    }

    LazyColumn(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            .fillMaxWidth()
    ) {
        items(items = status.status ?: return@LazyColumn) { item: Status ->
            Spacer(Modifier.height(10.dp))
            Text(
                text = "${item.name} - ${item.value}",
                color = Color.White,
//                fontSize = TextUnit(15f, TextUnitType.Sp)
            )
        }
    }
}

@Composable
fun MyInfoEquipment(viewModel: MainViewModel) {
    val equipment by viewModel.equipment.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getEquipment()
    }

    equipment.equipment?.let {
        LazyColumn(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            items(items = equipment.equipment ?: return@LazyColumn) {
                ItemCard(ItemRows(it)) {

                }
            }
        }
    } ?: CircularProgressIndicator()
}

@Composable
fun MyInfoAvatar(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    val avatar by viewModel.avatar.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getAvatar()
    }

    LazyColumn(
        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        items(items = avatar.avatar ?: return@LazyColumn) {
//            ItemCard(ItemRows(it)) { }
            ItemCardWithSubSlot(it, viewModel, stateViewModel)
        }
    }
}

@Composable
fun MyInfoBuffEquipment(viewModel: MainViewModel) {
    fun getDesc(option: Option) : String {
        val values = option.values
        var result = option.desc
        (1 .. values.size).forEach {
            result = result.replace("{value$it}", values[it-1])
        }
        return result.also { Log.d("getDesc() result > $it") }
    }
    val buffEquipment by viewModel.buffSkillEquip.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getBuffSkillEquip()
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
                ItemCard(ItemRows(it)) { }
            }
        }
    }
}

@Composable
fun MyInfoFlag(viewModel: MainViewModel, stateViewModel: DhStateViewModel) {
    val flag by viewModel.flag.collectAsState()
    val itemInfo by viewModel.itemInfo.collectAsState()
    val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
    val itemCardClickLambda = { itemId: String ->
        viewModel.getItemInfo(itemId)
        stateViewModel.setIsShowingItemInfoDialog(true)
    }

    LaunchedEffect(Unit) {
        viewModel.getFlag()
    }

    Column {
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
fun MyInfoCreature(viewModel: MainViewModel) {
    val creature by viewModel.creature.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCreature()
    }

    Column {
        ItemCard(ItemRows(creature.creature ?: return)) { }
        LazyColumn(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp)
        ) {
            items(items = creature.creature?.artifact ?: return@LazyColumn) {
                ItemCard(ItemRows(it)) { }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AuctionInfoDialog(row: AuctionRows, stateViewModel: DhStateViewModel) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingAuctionResultDialog(false) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    stateViewModel.setIsShowingAuctionResultDialog(false)
                }
            ) {
                Column(
                    verticalArrangement = Arrangement.Center
                ) {
//                    Icon(painter = painterResource(R.drawable.ic_baseline_arrow_back_24), contentDescription = null)
                    Text(text = "닫기", color = Color.White)
                }
            }
        },
        text = {
            Column {
                ItemCard(ItemRows(row)) { }
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_count),
                    value = row.count.toString()
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_current_price),
                    value = row.currentPrice.toString().makeComma()
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_unit_price),
                    value = row.unitPrice.toString().makeComma()
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_expire_time),
                    value = row.expireDate
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_available_level),
                    value = row.itemAvailableLevel.toString()
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_rarity),
                    value = row.itemRarity
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_reinforce),
                    value = row.reinforce.toString()
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_refine),
                    value = row.refine.toString()
                )
                AuctionInfoText(
                    label = stringResource(R.string.text_auction_item_adventure_fame),
                    value = row.adventureFame.toString()
                )
            }
        }
    )
}

@Composable
fun AuctionInfoText(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = Color.White,
//            fontSize = TextUnit(20f, TextUnitType.Sp),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            color = Color.White
        )
    }
}

@Composable
fun ItemSearchField(modifier: Modifier, searchClickCallback: () -> Unit, settingClickCallback: () -> Unit) {
    Row(modifier = modifier){
        Image(
            modifier = Modifier.clickable(onClick = searchClickCallback),
            painter = painterResource(R.drawable.ic_baseline_search_24),
            contentDescription = null,
        )

        Spacer(Modifier.width(15.dp))
        Image(
            modifier = Modifier.clickable(onClick = settingClickCallback),
            painter = painterResource(R.drawable.ic_baseline_settings_24),
            contentDescription = null
        )
    }
}

@Composable
fun AuctionSettingDialog(stateViewModel: DhStateViewModel) {
    val descList by remember { mutableStateOf(listOf("내림차순", "오름차순")) }
    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingAuctionSettingDialog(false) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { stateViewModel.setIsShowingAuctionSettingDialog(false) }
            ) {
                Text(text = "닫기", color = Color.White)
            }
        },
        text = {
            Column {
                SettingRadioButton("정렬순서", descList) {
                    when(it) {
                        "내림차순" -> stateViewModel.setPriceSort("desc")
                        "오름차순" -> stateViewModel.setPriceSort("asc")
                    }
                }
            }
        }
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HideKeyboard() = LocalSoftwareKeyboardController.current?.hide()