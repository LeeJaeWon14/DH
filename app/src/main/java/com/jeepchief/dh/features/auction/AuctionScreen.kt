package com.jeepchief.dh.features.auction

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.jeepchief.dh.DHApplication
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.network.dto.AuctionRows
import com.jeepchief.dh.core.network.dto.ItemRows
import com.jeepchief.dh.core.util.convertRarityColor
import com.jeepchief.dh.core.util.makeComma
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.navigation.BaseScreen
import com.jeepchief.dh.features.main.navigation.HideKeyboard
import com.jeepchief.dh.features.main.navigation.ItemCard
import com.jeepchief.dh.features.main.navigation.ItemSearchField
import com.jeepchief.dh.features.main.navigation.SettingRadioButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun AuctionScreen(
    auctionViewModel: DhAuctionViewModel = hiltViewModel(),
    stateViewModel: DhStateViewModel = hiltViewModel()
) {
    BaseScreen {
        var textChanged by remember { mutableStateOf("") }
        val isShowingAuctionResultDialog by stateViewModel.isShowingAuctionResultDialog.collectAsState()
        val isShowingAuctionSettingDialog by stateViewModel.isShowingAuctionSettingDialog.collectAsState()
        val auction by auctionViewModel.auction.collectAsState()
        var index by remember { mutableStateOf(0) }
        var isHideKeyboard by remember { mutableStateOf(false) }
        val context = LocalContext.current

        val searchAction = {
            if(textChanged.isNotEmpty()) {
                isHideKeyboard = true
                auctionViewModel.getAuction(
                    sort = stateViewModel.priceSort.value,
                    itemName = textChanged,
                    q = stateViewModel.rarityType.value.run {
                        if(this == context.getString(R.string.text_rarity_all)) ""
                        else this
                    }
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
                                val itemName =
                                    if(row.fame == 0)
                                        "${row.itemName}\r\n(Lv. ${row.itemAvailableLevel})"
                                    else
                                        "${row.itemName} [+${row.reinforce}/+${row.refine}]\r\n(Lv. ${row.itemAvailableLevel})"
                                Text(
                                    text = itemName,
                                    color = Color(row.itemRarity.convertRarityColor()),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = TextUnit(15f, TextUnitType.Sp)
                                )

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
                if(row.itemType != "스태커블") {
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
                        value = row.fame.toString()
                    )
                }
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
fun AuctionSettingDialog(stateViewModel: DhStateViewModel) {
    val sortList = listOf(
        stringResource(R.string.text_auction_sort_desc),
        stringResource(R.string.text_auction_sort_asc)
    )
    val checkedSort by stateViewModel.priceSort.collectAsState()
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
                SettingRadioButton(
                    title = stringResource(R.string.text_auction_price_sort),
                    list = sortList,
                    initChecked = checkedSort
                ) {
                    when(it) {
                        sortList[0] -> stateViewModel.setPriceSort("desc")
                        sortList[1] -> stateViewModel.setPriceSort("asc")
                    }
                }
                SettingRadioButton(
                    title = stringResource(R.string.text_auction_item_rarity),
                    list = rarityType,
                    initChecked = checkedRarityType
                ) {
                    stateViewModel.setRarityType(it)
                }
            }
        }
    )
}