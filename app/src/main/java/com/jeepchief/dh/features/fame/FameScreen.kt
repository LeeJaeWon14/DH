package com.jeepchief.dh.features.fame

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.gson.Gson
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.util.Pref
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.activity.CharacterCard
import com.jeepchief.dh.features.main.activity.MainActivity
import com.jeepchief.dh.features.main.navigation.BaseScreen
import com.jeepchief.dh.features.main.navigation.HideKeyboard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FameScreen(
    viewModel: MainViewModel = hiltViewModel(),
    stateViewModel: DhStateViewModel = hiltViewModel()
) {
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