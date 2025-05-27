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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
import com.jeepchief.dh.databinding.ActivityMainBinding
import com.jeepchief.dh.features.main.DhStateViewModel
import com.jeepchief.dh.features.main.MainViewModel
import com.jeepchief.dh.features.main.navigation.AuctionScreen
import com.jeepchief.dh.features.main.navigation.CharacterScreen
import com.jeepchief.dh.features.main.navigation.DhScreen
import com.jeepchief.dh.features.main.navigation.DictionaryScreen
import com.jeepchief.dh.features.main.navigation.ItemSearchScreen
import com.jeepchief.dh.features.main.navigation.MainScreen
import com.jeepchief.dh.features.main.navigation.MyInfoScreen
import com.jeepchief.dh.features.main.navigation.TimeLineScreen
import com.jeepchief.dh.ui.theme.DefaultBackColor
import com.jeepchief.dh.ui.theme.DefaultDialogColor
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.exitProcess

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var isServerDownloadComplete = false

    private val viewModel: MainViewModel by viewModels()
    private val stateViewModel: DhStateViewModel by viewModels()

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
                    modifier = Modifier.fillMaxSize()
                ) { padding ->

                    if(Pref.getString(Pref.CHARACTER_INFO)?.isEmpty() == true) {
                        ShowCharacterSearchDialog(
                            viewModel, stateViewModel
                        )
                    } else {
                        val info = Gson().fromJson(Pref.getString(Pref.CHARACTER_INFO), CharacterRows::class.java)
                        Log.d("""
                            info : $info
                        """.trimIndent())
                        viewModel.setNowCharacterInfo(info)
                    }

                    AppNavHost(navHost, padding, viewModel, stateViewModel)
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
            }
        }

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // init UI
//        binding.apply {
//            navController = findNavController(R.id.nav_host_fragment)
//
//        }
        checkNetworkEnable()
//        observerViewModel()

//        checkPref()
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

//    private fun checkPref() {
//        if(Pref.getString(Pref.CHARACTER_INFO)?.isEmpty() == true) {
//            showCharacterSearchDialog()
//        }
//        else {
//            val jsonString = Pref.getString(Pref.CHARACTER_INFO)
//            Gson().fromJson(jsonString, CharacterRows::class.java).run {
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        val row = viewModel.dfService.getCharacters(serverId, characterName)
//                        viewModel.nowCharacterInfo.postValue(row.characterRows[0])
//                    } catch(e: Exception) {
//                        e.printStackTrace()
//
//                        withContext(Dispatchers.Main) {
//                            // System Dialog
//                            AlertDialog.Builder(this@MainActivity)
//                                .setMessage(getString(R.string.error_msg_system_check_msg))
//                                .setPositiveButton("종료") { _, _ ->
//                                    finishAffinity()
//                                    exitProcess(0)
//                                }
//                                .show()
//                        }
//                    }
//                }
//            }
//        }
//        downloadMetadata()
//    }

//    private fun downloadMetadata() {
////        binding.progressBar.isVisible = true
//        ProgressDialog.showProgressDialog(this)
//        viewModel.run {
//            getServerList()
//        }
//    }

//    private fun observerViewModel() {
//        viewModel.run {
//            servers.observe(this@MainActivity) { dto ->
//                ProgressDialog.dismissDialog()
//                binding.progressBar.isVisible = false
//                Log.e("server list is download complete")
//            }
//
//            characters.observe(this@MainActivity) { dto ->
//                val dlgView = LayoutDialogSelectCharacterBinding.inflate(layoutInflater)
//                val dlg = AlertDialog.Builder(this@MainActivity).create().apply {
//                    setView(dlgView.root)
//                    setCancelable(false)
//                }
//                dlgView.apply {
//                    rvCharacterList.apply {
//                        layoutManager = LinearLayoutManager(this@MainActivity)
//                        adapter = SelectCharacterAdapter(dto.characterRows, servers.value!!) { dlg.dismiss() }
//                    }
//                }
//                dlg.show()
//            }
//
//            nowCharacterInfo.observe(this@MainActivity) { row ->
//                Log.e(row.toString())
//                supportActionBar?.apply {
//                    title = row.characterName.plus("_Lv. ${row.level}")
//                    subtitle = row.jobName.plus(" - ${row.jobGrowName}")
//                }
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    row.run {
//                        DhDatabase.getInstance(this@MainActivity).getCharactersDAO().run {
//                            selectCharacterId(characterId)?.let {
//                                // Need code update on room.
//                                Log.e("Already stored id")
//                            } ?: run {
//                                insertCharacter(
//                                    CharactersEntity(
//                                    serverId, characterId, characterName, level, jobId, jobGrowId, jobName, jobGrowName
//                                )
//                                ).also { Log.e("insert into entity") }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

//    private fun showCharacterSearchDialog() {
//        val dlgView = LayoutDialogSearchCharacterBinding.inflate(layoutInflater)
//        val dlg = AlertDialog.Builder(this).create().apply {
//            setView(dlgView.root)
//            setCancelable(false)
//        }
//
//        dlgView.apply {
//            btnInsertOk.setOnClickListener {
//                viewModel.getCharacters(name = edtInsertId.text.toString())
//                dlg.dismiss()
//            }
//        }
//
//        dlg.show()
//    }
//
//    fun updateSimpleInfo(row: CharacterRows) {
//        viewModel.nowCharacterInfo.value = row
//    }
//
//    fun updateCharacterFragmentList(list: List<CharacterRows>) {
//        val fragments = supportFragmentManager.fragments
//        fragments.forEach { fragment ->
//            if(fragment is ChangeCharacterFragment) {
//                fragment.updateList(list)
//            }
//        }
//    }

    /*
    back button in fragment is will adding scroll behavior
     */
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item.itemId) {
//            R.id.menu_logout -> {
//                AlertDialog.Builder(this).apply {
//                    setMessage("로그아웃 하시겠습니까?")
//                    setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
//                        if(Pref.getInstance(this@MainActivity)?.removeValue(Pref.CHARACTER_INFO) == true) {
//                            finishAffinity()
//                            startActivity(Intent(this@MainActivity, MainActivity::class.java))
//                        }
//                        else {
//                            Toast.makeText(this@MainActivity, "알 수 없는 에러 발생, 로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    setNegativeButton("취소", null)
//                }.show()
//            }
//        }
//        return true
//    }

//    private fun checkMetaDataDownload() {
//        if(isServerDownloadComplete) {
//            binding.progressBar.isVisible = false
//            Pref.getInstance(this@MainActivity)?.setValue(Pref.FIRST_LOGIN, true)
//        }
//    }
//
//    fun updateActionbar() {
//        when(supportActionBar?.isShowing) {
//            true -> supportActionBar?.hide()
//            else -> supportActionBar?.show()
//        }
//    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhTopBar(viewModel: MainViewModel) {
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
    viewModel: MainViewModel,
    stateViewModel: DhStateViewModel
) {
    var characterName by remember { mutableStateOf("") }
    val activity = LocalActivity.current ?: throw IllegalStateException()
    val isShowingCharacterSelectDialog by stateViewModel.isShowingCharacterSelectDialog.collectAsState()
    val itemList by viewModel.characters.collectAsState()

    AlertDialog(
        onDismissRequest = { stateViewModel.setIsShowingCharacterSearchDialog(false) },
        confirmButton = {
            Button(onClick = {
                viewModel.getCharacters(name = characterName)
                stateViewModel.run {
                    setIsShowingCharacterSelectDialog(true)
                    setIsShowingCharacterSearchDialog(false)
                }
            }) {
                Text("검색")
            }
        },
        dismissButton = {
            Button(onClick = {
//                viewModel.setIsShowingCharacterSearchDialog(false)
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
        ShowCharacterSelectDialog(viewModel, stateViewModel, itemList.characterRows)
    }
}

@Composable
fun ShowCharacterSelectDialog(
    viewModel: MainViewModel,
    stateViewModel: DhStateViewModel,
    charList: List<CharacterRows>
) {
//    val charList = viewModel
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
                        Pref.setValue(Pref.CHARACTER_INFO, Gson().toJson(row))
                        viewModel.setNowCharacterInfo(row)
                        viewModel.insertCharacter(row)
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

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CharacterCard(character: CharacterRows, dismissCallback: () -> Unit) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dismissCallback() }
    ) {
        GlideImage(
            model = String.format(NetworkConstants.CHARACTER_URL, character.serverId, character.characterId, 0),
            contentDescription = "Character Card",
            modifier = Modifier.weight(0.2f),
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = character.serverId)
            Text(text = "${character.characterName}(Lv.${character.level})")
            Text(text = "${character.jobName} - ${character.jobGrowName}")
        }
    }
}

@Composable
fun AppNavHost(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
    stateViewModel: DhStateViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = DhScreen.Main.route,
        modifier = Modifier.background(colorResource(R.color.back_color))
    ) {
        composable(DhScreen.Main.route) {
            MainScreen(navHostController)
        }
        composable(DhScreen.MyInfo.route) {
            MyInfoScreen(viewModel, stateViewModel)
        }
        composable(DhScreen.ItemSearch.route) {
            ItemSearchScreen(viewModel, stateViewModel)
        }
        composable(DhScreen.Auction.route) {
            AuctionScreen(viewModel, stateViewModel)
        }
        composable(DhScreen.Character.route) {
            CharacterScreen(viewModel, stateViewModel)
        }
        composable(DhScreen.Dictionary.route) {
            DictionaryScreen(viewModel, stateViewModel)
        }
        composable(DhScreen.TimeLIne.route) {
            TimeLineScreen(viewModel, stateViewModel)
        }
    }
}

@Composable
fun ExitDialog(activity: Activity, stateViewModel: DhStateViewModel) {
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