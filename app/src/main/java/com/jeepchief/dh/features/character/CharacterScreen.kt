package com.jeepchief.dh.features.character

import android.content.Intent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.jeepchief.dh.core.util.Pref
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.DhMainViewModel
import com.jeepchief.dh.features.main.activity.CharacterCard
import com.jeepchief.dh.features.main.activity.MainActivity
import com.jeepchief.dh.features.main.navigation.BaseScreen

@Composable
fun CharacterScreen(
    viewModel: DhMainViewModel = hiltViewModel(),
    stateViewModel: DhStateViewModel = hiltViewModel()
) {
    BaseScreen {
//        val isShowingCharacterSearchDialog by stateViewModel.isShowingCharacterSearchDialog.collectAsState()
        val isShowingCharacterRemoveDialog by stateViewModel.isShowingCharacterRemoveDialog.collectAsState()
        val characterList by viewModel.allCharacters.collectAsState()
        val context = LocalContext.current
        var deleteTarget by remember { mutableStateOf("") }

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