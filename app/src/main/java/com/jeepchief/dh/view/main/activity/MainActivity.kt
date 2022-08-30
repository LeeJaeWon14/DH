package com.jeepchief.dh.view.main.activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jeepchief.dh.R
import com.jeepchief.dh.databinding.ActivityMainBinding
import com.jeepchief.dh.databinding.LayoutDialogSearchCharacterBinding
import com.jeepchief.dh.databinding.LayoutDialogSelectCharacterBinding
import com.jeepchief.dh.model.database.DhDatabase
import com.jeepchief.dh.model.database.characters.CharactersEntity
import com.jeepchief.dh.model.rest.dto.CharacterRows
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.Pref
import com.jeepchief.dh.view.character.ChangeCharacterFragment
import com.jeepchief.dh.view.main.adapter.SelectCharacterAdapter
import com.jeepchief.dh.viewmodel.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var isServerDownloadComplete = false

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init UI
        binding.apply {
            navController = findNavController(R.id.nav_host_fragment)

//            actionBar?.hide()
//            setSupportActionBar(toolbar)
//            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        observerViewModel()

        checkPref()
    }

    private fun checkPref() {
        if(Pref.getInstance(this)?.getString(Pref.CHARACTER_INFO)?.isEmpty() == true) {
            showCharacterSearchDialog()
        }
        else {
            val jsonString = Pref.getInstance(this)?.getString(Pref.CHARACTER_INFO)
            Gson().fromJson(jsonString, CharacterRows::class.java).run {
                CoroutineScope(Dispatchers.IO).launch {
                    val row = viewModel.dfService.getCharacters(serverId, characterName)
                    viewModel.mySimpleInfo.postValue(row.characterRows[0])
                }
            }
        }
        downloadMetadata()
    }

    private fun downloadMetadata() {
        binding.progressBar.isVisible = true
        viewModel.run {
            getServerList()
        }
    }

    private fun observerViewModel() {
        viewModel.run {
            servers.observe(this@MainActivity) { dto ->
//                isServerDownloadComplete = true
//                checkMetaDataDownload()
                binding.progressBar.isVisible = false
                Log.e("server list is download complete")
            }

            characters.observe(this@MainActivity) { dto ->
                val dlgView = LayoutDialogSelectCharacterBinding.inflate(layoutInflater)
                val dlg = AlertDialog.Builder(this@MainActivity).create().apply {
                    setView(dlgView.root)
                    setCancelable(false)
                }
                dlgView.apply {
                    rvCharacterList.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = SelectCharacterAdapter(dto.characterRows, dlg, servers.value!!)
                    }
                }
                dlg.show()
            }

            mySimpleInfo.observe(this@MainActivity) { row ->
                Log.e(row.toString())
                supportActionBar?.apply {
                    title = row.characterName.plus("_Lv. ${row.level}")
                    subtitle = row.jobName.plus(" - ${row.jobGrowName}")
                }

                CoroutineScope(Dispatchers.IO).launch {
                    row.run {
                        DhDatabase.getInstance(this@MainActivity).getCharactersDAO().run {
                            selectCharacterId(characterId)?.let {
                                Log.e("Already stored id")
                            } ?: run {
                                insertCharacter(CharactersEntity(
                                    serverId, characterId, characterName, level, jobId, jobGrowId, jobName, jobGrowName
                                )).also { Log.e("insert into entity") }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showCharacterSearchDialog() {
        val dlgView = LayoutDialogSearchCharacterBinding.inflate(layoutInflater)
        val dlg = AlertDialog.Builder(this).create().apply {
            setView(dlgView.root)
            setCancelable(false)
        }

        dlgView.apply {
            btnInsertOk.setOnClickListener {
                viewModel.getCharacters(name = edtInsertId.text.toString())
                dlg.dismiss()
            }
        }

        dlg.show()
    }

    fun updateSimpleInfo(row: CharacterRows) {
        viewModel.mySimpleInfo.value = row
    }

    fun updateCharacterFragmentList(list: List<CharacterRows>) {
        val newList = mutableListOf<CharactersEntity>()
        list.forEach {
            it.run {
                newList.add(CharactersEntity(serverId, characterId, characterName, level, jobId, jobGrowId, jobName, jobGrowName))
            }
        }
        val fragments = supportFragmentManager.fragments
        fragments.forEach { fragment ->
            if(fragment is ChangeCharacterFragment) {
                fragment.updateList(newList)
            }
        }
    }

    /*
    back button in fragment is will adding scroll behavior
     */

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_logout -> {
                AlertDialog.Builder(this).apply {
                    setMessage("로그아웃 하시겠습니까?")
                    setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
                        if(Pref.getInstance(this@MainActivity)?.removeValue(Pref.CHARACTER_INFO) == true) {
                            finishAffinity()
                            startActivity(Intent(this@MainActivity, MainActivity::class.java))
                        }
                        else {
                            Toast.makeText(this@MainActivity, "알 수 없는 에러 발생, 로그아웃에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    setNegativeButton("취소", null)
                }.show()
            }
        }
        return true
    }

    private fun checkMetaDataDownload() {
        if(isServerDownloadComplete) {
            binding.progressBar.isVisible = false
            Pref.getInstance(this@MainActivity)?.setValue(Pref.FIRST_LOGIN, true)
        }
    }

    fun updateActionbar() {
        when(supportActionBar?.isShowing) {
            true -> supportActionBar?.hide()
            else -> supportActionBar?.show()
        }

//        actionBar?.hide()
    }
//
//    fun showActionbar() {
//        actionBar?.show()
//    }
}