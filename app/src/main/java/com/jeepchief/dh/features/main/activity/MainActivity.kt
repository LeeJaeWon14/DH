package com.jeepchief.dh.features.main.activity

import android.app.Activity
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.gson.Gson
import com.jeepchief.dh.R
import com.jeepchief.dh.core.network.NetworkConstants
import com.jeepchief.dh.core.network.dto.CharacterRows
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.core.util.Pref
import com.jeepchief.dh.core.util.convertServerName
import com.jeepchief.dh.features.auction.AuctionScreen
import com.jeepchief.dh.features.character.CharacterScreen
import com.jeepchief.dh.features.fame.FameScreen
import com.jeepchief.dh.features.main.DhMainStateViewModel
import com.jeepchief.dh.features.main.DhMainViewModel
import com.jeepchief.dh.features.main.navigation.DhScreen
import com.jeepchief.dh.features.main.navigation.ItemSearchScreen
import com.jeepchief.dh.features.main.navigation.MainScreen
import com.jeepchief.dh.features.myinfo.MyInfoScreen
import com.jeepchief.dh.features.timeline.TimeLineScreen
import com.jeepchief.dh.ui.theme.DefaultBackColor
import com.jeepchief.dh.ui.theme.DefaultDialogColor
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: DhMainViewModel by viewModels()
    private val stateViewModel: DhMainStateViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val navHost = rememberNavController()
            val navBackStackEntry by navHost.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val isShowingCharacterSearchDialog by stateViewModel.isShowingCharacterSearchDialog.collectAsState()
            val isShowingExitDialog by stateViewModel.isShowingExitDialog.collectAsState()
            val isShowingAppBar by stateViewModel.isShowingAppBar.collectAsState()

            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = DefaultBackColor,
                    secondary = Color.White,
                    surface = DefaultDialogColor,
                    onSurface = Color.White,
                )
            ) {
                Scaffold(
                    topBar = {
                        if(isShowingAppBar) {
                            DhTopBar(viewModel)
                        }
                    },
                    floatingActionButton = {
                        if(currentRoute == DhScreen.Character.route) {
                            Button(onClick = {
                                stateViewModel.setIsShowingCharacterSearchDialog(true)
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_baseline_add_24),
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = DefaultBackColor)
                ) { padding ->

                    if(Pref.getString(Pref.CHARACTER_INFO)?.isEmpty() == true) {
                        stateViewModel.setIsShowingCharacterSearchDialog(true)
                    } else {
                        val info = Gson().fromJson(Pref.getString(Pref.CHARACTER_INFO), CharacterRows::class.java)
                        Log.d("""
                            info : $info
                        """.trimIndent())
                        viewModel.setNowCharacterInfo(info)
                    }

                    AppNavHost(navHost)
                }

                BackHandler {
                    if(currentRoute == DhScreen.Main.route) {
                        stateViewModel.setIsShowingExitDialog(true)
                    } else {
                        navHost.popBackStack()
                        stateViewModel.setIsShowingAppBar(true)
                    }
                }
                
                if(isShowingExitDialog) {
                    ExitDialog(this, stateViewModel)
                }

                if(isShowingCharacterSearchDialog) {
                    ShowCharacterSearchDialog(
                        viewModel, stateViewModel, navHost
                    ) { row ->

                        if(Pref.getString(Pref.CHARACTER_INFO)?.isEmpty() == true) {
                            Pref.setValue(Pref.CHARACTER_INFO, Gson().toJson(row))
                            viewModel.run {
                                setNowCharacterInfo(row)
                                insertCharacter(row)
                            }
                        }
                        else viewModel.insertCharacter(row)
                    }
                }
            }
        }
        checkNetworkEnable()
    }

    /**
     * Network 연결 체크
     */
    private fun checkNetworkEnable() {
        val connectivityManager = getSystemService(ConnectivityManager::class.java)
//        val currentNetwork = connectivityManager.activeNetwork

        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.e("network available")
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.e("network lost")
//                Toast.makeText(this@MainActivity, getString(R.string.error_msg_not_connected_network), Toast.LENGTH_SHORT).show()
                AlertDialog.Builder(this@MainActivity)
                    .setMessage(getString(R.string.error_msg_not_connected_network))
                    .setPositiveButton("종료") { _, _ ->
                        finishAffinity()
                        exitProcess(0)
                    }
                    .show()
            }
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhTopBar(viewModel: DhMainViewModel) {
    val characterInfo by viewModel.nowCharacterInfo.collectAsState()

    if(characterInfo.level == -1) return
    TopAppBar(
        modifier = Modifier.fillMaxWidth(),
        title = {
            Column {
                Text(
                    text = "${characterInfo.characterName}_Lv. ${characterInfo.level}",
                    fontSize = TextUnit(20f, TextUnitType.Sp),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "${characterInfo.jobName} - ${characterInfo.jobGrowName}",
                    fontSize = TextUnit(15f, TextUnitType.Sp),
                    color = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCharacterSearchDialog(
    viewModel: DhMainViewModel,
    stateViewModel: DhMainStateViewModel,
    navHostController: NavHostController,
    dismissCallback: (CharacterRows) -> Unit
) {
    var characterName by remember { mutableStateOf("") }
    val activity = LocalActivity.current ?: throw IllegalStateException()
    val isShowingCharacterSelectDialog by stateViewModel.isShowingCharacterSelectDialog.collectAsState()
    val itemList by viewModel.characters.collectAsState()

    val navBackStackEntry by navHostController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingCharacterSearchDialog(false) },
        confirmButton = {
            Button(onClick = {
                viewModel.getCharacters(name = characterName)
                stateViewModel.setIsShowingCharacterSelectDialog(true)
            }) {
                Text("검색")
            }
        },
        dismissButton = {
            Button(onClick = {
                if(currentRoute == DhScreen.Character.route)
                    stateViewModel.setIsShowingCharacterSearchDialog(false)
                else
                    activity.finishAffinity()
            }) {
                Text("닫기")
            }
        },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        text = {
            OutlinedTextField(
                value = characterName,
                label = { Text("캐릭터명") },
                onValueChange = { characterName = it }
            )
        }
    )

    if(isShowingCharacterSelectDialog && itemList.characterRows.isNotEmpty()) {
        ShowCharacterSelectDialog(viewModel, stateViewModel, itemList.characterRows, dismissCallback)
    }
}

@Composable
fun ShowCharacterSelectDialog(
    viewModel: DhMainViewModel,
    stateViewModel: DhMainStateViewModel,
    charList: List<CharacterRows>,
    dismissCallback: (CharacterRows) -> Unit
) {
    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingCharacterSelectDialog(false) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        confirmButton = {
            Button(onClick = {
                stateViewModel.setIsShowingCharacterSelectDialog(false)
            }) {
                Text("닫기")
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(charList) { row ->
                    CharacterCard(row) {
                        dismissCallback(row)
                        stateViewModel.run {
                            setIsShowingCharacterSelectDialog(false)
                            setIsShowingCharacterSearchDialog(false)
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun CharacterCard(character: CharacterRows, longClickCallback: ((String) -> Unit)? = null, dismissCallback: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.White,
                shape = RoundedCornerShape(20.dp)
            )
//            .clickable { dismissCallback() }
            .combinedClickable(
                onLongClick = { longClickCallback?.invoke(character.characterId) },
                onClick = dismissCallback
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(
            model = String.format(NetworkConstants.CHARACTER_URL, character.serverId, character.characterId, 0),
            contentDescription = "Character Card",
            modifier = Modifier.weight(0.5f),
//                .border(width = 1.dp, color = Color.White),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier
            .border(1.dp, color = Color.White)
            .width(1.dp)
            .padding(top = 20.dp, bottom = 20.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column(
            modifier = Modifier.weight(0.5f)
        ) {
            Text(text = character.serverId.convertServerName(), color = Color.White)
            Text(text = "${character.characterName}(Lv.${character.level})", color = Color.White)
            Text(text = "${character.jobName}\n${character.jobGrowName}", color = Color.White)
            if(character.fame > 0) {
                Text(text = "명성 : ${character.fame}", color = Color.White)
            }
        }
    }
    Spacer(Modifier.height(10.dp))
}

val LocalNavController = staticCompositionLocalOf<NavController> {
    error("NavController not provided")
}

@Composable
fun AppNavHost(
    navHostController: NavHostController,
) {
    NavHost(
        navController = navHostController,
        startDestination = DhScreen.Main.route,
        modifier = Modifier.background(colorResource(R.color.back_color)),
        enterTransition = {
            fadeIn(tween(900)) +
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
        },
        exitTransition = {
            fadeOut(tween(900)) +
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(300)
                    )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        composable(DhScreen.Main.route) {
            MainScreen(navHostController)
        }
        composable(DhScreen.MyInfo.route) {
            MyInfoScreen()
        }
        composable(DhScreen.ItemSearch.route) {
            ItemSearchScreen()
        }
        composable(DhScreen.Auction.route) {
            AuctionScreen()
        }
        composable(DhScreen.Character.route) {
            CharacterScreen()
        }
        composable(DhScreen.FameSearch.route) {
            FameScreen()
        }
        composable(DhScreen.TimeLIne.route) {
            TimeLineScreen()
        }
    }
}

@Composable
fun ExitDialog(activity: Activity, stateViewModel: DhMainStateViewModel) {
    AlertDialog(
        modifier = Modifier.background(colorResource(R.color.default_dialog_color)),
        onDismissRequest = { stateViewModel.setIsShowingExitDialog(false) },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
        title = {
            Text(text = "안내")
        },
        text = {
            Text(text = "앱을 종료하시겠습니까?", color = Color.White)
        },
        confirmButton = {
            Button(onClick = {
                activity.finishAffinity()
            }) {
                Text(text = "종료")
            }
        },
        dismissButton = {
            Button(onClick = { stateViewModel.setIsShowingExitDialog(false) }) {
                Text(text = "취소")
            }
        },
        shape = RoundedCornerShape(50.dp)
    )
}