package com.jeepchief.dh.view.main.activity

import android.os.Bundle
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
import com.jeepchief.dh.model.rest.dto.CharacterRows
import com.jeepchief.dh.util.Log
import com.jeepchief.dh.util.Pref
import com.jeepchief.dh.view.main.adapter.SelectCharacterAdapter
import com.jeepchief.dh.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var isCheckedServers = false

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        supportActionBar?.hide()

        // init UI
        binding.apply {
            navController = findNavController(R.id.nav_host_fragment)
        }
        observerViewModel()

        checkPref()
    }

    private fun checkPref() {
        if(!Pref.getInstance(this)?.getBoolean(Pref.FIRST_LOGIN)!!) {
            downloadMetadata()
        }
        if(Pref.getInstance(this)?.getString(Pref.CHARACTER_INFO)?.isEmpty() == true) {
            showCharacterSearchDialog()
        }
        else {
            val jsonString = Pref.getInstance(this)?.getString(Pref.CHARACTER_INFO)
            Gson().fromJson(jsonString, CharacterRows::class.java).run {
                viewModel.mySimpleInfo.value = this
            }
        }
    }

    private fun downloadMetadata() {
        binding.progressBar.isVisible = true
        viewModel.run {
            getServerList()
            getJobs()
        }
    }

    private fun observerViewModel() {
        viewModel.run {
            servers.observe(this@MainActivity) { dto ->
                binding.progressBar.isVisible = false

                Log.e("server list is download complete")
            }

            characters.observe(this@MainActivity) { dto ->
                val dlgView = LayoutDialogSelectCharacterBinding.inflate(layoutInflater)
                val dlg = AlertDialog.Builder(this@MainActivity).create().apply {
                    setView(dlgView.root)
//                    setCancelable(false)
                }
                dlgView.apply {
                    rvCharacterList.apply {
                        layoutManager = LinearLayoutManager(this@MainActivity)
                        adapter = SelectCharacterAdapter(dto.characterRows, dlg, viewModel.mySimpleInfo)
                    }
                }
                dlg.show()
//                Pref.getInstance(this@MainActivity)?.setValue(Pref.FIRST_LOGIN, true)
            }

            jobs.observe(this@MainActivity) { dto ->
                Log.e("Job list is download complete")
            }

            mySimpleInfo.observe(this@MainActivity) { row ->
                updateActionbarTitle(row)
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
                viewModel.getCharacters(edtInsertId.text.toString())
                dlg.dismiss()
            }
        }

        dlg.show()
    }

    fun updateActionbarTitle(row: CharacterRows) {
        row.run {
            supportActionBar?.apply {
                title = characterName.plus("_Lv. $level")
                subtitle = jobName.plus(" - $jobGrowName")
            }
        }
    }

    /*
    back button in fragment is will adding scroll behavior
     */
}