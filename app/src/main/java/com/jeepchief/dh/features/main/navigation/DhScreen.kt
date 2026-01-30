package com.jeepchief.dh.features.main.navigation

import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import com.jeepchief.dh.R
import com.jeepchief.dh.core.database.recent.RecentFameEntity
import com.jeepchief.dh.core.database.recent.RecentItemEntity
import com.jeepchief.dh.core.database.recent.RecentSearchItem
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.network.dto.Avatar
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.network.dto.ItemsDTO
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.convertRarityColor
import com.jeepchief.dh.core.util.toDateFormat
import com.jeepchief.dh.core.util.toWordType
import com.jeepchief.dh.features.main.DhMainStateViewModel
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.DhMainViewModel
import com.jeepchief.dh.features.main.activity.MainActivity
import com.jeepchief.dh.ui.theme.White50
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemSearchScreen(
    viewModel: DhMainViewModel = hiltViewModel(),
    stateViewModel: DhStateViewModel = hiltViewModel()
) {
    BaseScreen {
        val context = LocalContext.current
        var searchChanged by remember { mutableStateOf("") }
        val itemSearch by viewModel.itemSearch.collectAsState()
        val itemInfo by viewModel.itemInfo.collectAsState()
        val isShowingItemInfoDialog by stateViewModel.isShowingItemInfoDialog.collectAsState()
        val isShowingSearchSettingDialog by stateViewModel.isShowingSearchSettingDialog.collectAsState()
        var mWordType by remember { mutableStateOf(context.getString(R.string.text_word_type_front)) }
        var mRarity by remember { mutableStateOf("") }
        var isHideKeyboard by remember { mutableStateOf(false) }
        var isShowingNotFoundSearchResult by remember { mutableStateOf(false) }
        val recentSearchList by viewModel.recentItems.collectAsState()
        var isDeleteRecentItemIndex by remember { mutableStateOf(-1) }

        val searchAction = {
            if(searchChanged.isNotEmpty()) {
                isHideKeyboard = true
                viewModel.getSearchItems(
                    searchChanged,
                    mWordType.toWordType(),
                    mRarity
                )
                viewModel.insertRecentItem(searchChanged)
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
                if(searchChanged.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        itemsIndexed(rows) { index, row ->
                            ItemCard(row) { itemId ->
                                viewModel.getItemInfo(itemId)
                                stateViewModel.setIsShowingItemInfoDialog(true)
                            }
                            if(index != rows.lastIndex) {
                                Divider(White50)
                            }
                        }
                    }
                }
            }

            if(searchChanged.isEmpty() && recentSearchList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                RecentItemSearchList(
                    recentSearchList,
                    itemClickCallback = { itemName ->
                        searchChanged = itemName
                        searchAction()
                    },
                    itemLongClickCallback = { index ->
//                        viewModel.deleteRecentItem(recentSearchList[index])
                        isDeleteRecentItemIndex = index
                    }
                )
            }
        }

        LaunchedEffect(isShowingNotFoundSearchResult) {
            if(isShowingNotFoundSearchResult) {
                Toast.makeText(context, "검색결과가 없습니다.", Toast.LENGTH_SHORT).show()
                isShowingNotFoundSearchResult = false
            }
        }

        if(isShowingItemInfoDialog) {
            ItemInfoDialog(itemInfo, stateViewModel)
        }

        if(isShowingSearchSettingDialog) {
            SearchSettingDialog(viewModel, stateViewModel) { wordType, rarity ->
                mWordType = wordType
                mRarity = rarity
            }
        }

        if(isDeleteRecentItemIndex != -1) {
            DeleteConfirmDialog(
                onConfirm = {
                    viewModel.deleteRecentItem(recentSearchList[isDeleteRecentItemIndex])
                    isDeleteRecentItemIndex = -1
                },
                onDismiss = { isDeleteRecentItemIndex = -1 }
            )
        }
    }
}

@Composable
fun DeleteConfirmDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = "확인", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "취소", color = Color.White)
            }
        },
        text = {
            Text(
                text = "삭제 하시겠습니까?",
                color = Color.White
            )
        }
    )
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
fun BaseScreen(isUsePadding: Boolean = true, content: @Composable () -> Unit) {
    val stateViewModel: DhMainStateViewModel = viewModel(LocalActivity.current as MainActivity)

    DisposableEffect(Unit) {
        stateViewModel.setIsShowingAppBar(false)
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
                end = if (isUsePadding) 10.dp else 0.dp,
                bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
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
fun ItemCard(row: ItemRows, onClick: ((String) -> Unit)? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 5.dp, bottom = 5.dp)
            .clickable(onClick = {
                onClick?.invoke(row.itemId)
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
            modifier = Modifier.size(40.dp)
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
                if(row.reinforce > 0)  "(+${row.reinforce}) ${row.itemName}" //\r\n(Lv. ${row.itemAvailableLevel})"
                else                    "${row.itemName}" //\r\n(Lv. ${row.itemAvailableLevel})"

            Text(
                text = itemDisplayText,
                color = Color(row.itemRarity.convertRarityColor()),
                fontWeight = FontWeight.Bold,
                fontSize = TextUnit(15f, TextUnitType.Sp)
            )
            if(row.itemType.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "${row.itemType}-${row.itemTypeDetail}",
                        color = Color.White
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
}

@Composable
fun SearchSettingDialog(viewModel: DhMainViewModel, stateViewModel: DhStateViewModel, resultCallback: (String, String) -> Unit) {
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
    Log.d("itemExplain :: ${dto.itemExplain}")
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
            Column {
                LazyColumn {
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

                        dto.tune?.firstOrNull()?.setPoint
                            ?.takeIf { it != 0 }
                            ?.let { point ->
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "세트포인트 $point",
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
            }
        }
    )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhModalBottomSheet(
    sheetState: SheetState,
    stateViewModel: DhStateViewModel,
    textList: List<String>,
    callbackList: List<() -> Unit>
) {
    ModalBottomSheet(
        onDismissRequest = { stateViewModel.setIsShowingBottomSheet(false) },
        sheetState = sheetState
    ) {
        for(i in textList.indices) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable(onClick = callbackList[i]),
                text = textList[i],
                color = Color.White
            )
        }
        Spacer(Modifier.height(50.dp))
    }
}

@Composable
fun DhCircularProgress() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun RecentItemSearchList(itemList: List<RecentItemEntity>, itemClickCallback: (String) -> Unit, itemLongClickCallback: (Int) -> Unit) {
    val realItemList = mutableListOf<RecentSearchItem>().apply {
        itemList.forEach { add(RecentSearchItem(it)) }
    }

    RecentList(realItemList, itemClickCallback, itemLongClickCallback)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecentList(itemList: List<RecentSearchItem>, itemClickCallback: (String) -> Unit, itemLongClickCallback: (Int) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
            .border(1.dp, Color.White, RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        item {
            Text(
                text = "최근 검색 기록",
                color = Color.White
            )
            Divider()
        }
        itemsIndexed(items = itemList) { idx, item ->
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp)
                    .combinedClickable(
                        onClick = { itemClickCallback.invoke(item.searchName) },
                        onLongClick = { itemLongClickCallback.invoke(idx) }     // 세 곳에서 동시에 쓰이고 사용되는 List가 다르기 때문에 콜백으로 item의 index만 전달
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = item.searchName,
                    color = Color.White
                )
                Text(
                    text = item.searchTime.toDateFormat(),
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun Divider(color: Color? = null) {
    Spacer(
        modifier = Modifier.fillMaxWidth()
            .height(1.dp)
            .border(
                width = 1.dp,
                color = color ?: Color.White
            )
    )
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HideKeyboard() = LocalSoftwareKeyboardController.current?.hide()