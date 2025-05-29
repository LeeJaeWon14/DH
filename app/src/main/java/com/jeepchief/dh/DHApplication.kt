package com.jeepchief.dh

import android.app.Application
import android.content.Context
import android.content.Intent
import com.jeepchief.dh.core.util.Log
import com.jeepchief.dh.features.main.activity.MainActivity
import dagger.hilt.android.HiltAndroidApp
import kotlin.system.exitProcess

@HiltAndroidApp
class DHApplication: Application() {
    companion object {
        private lateinit var context: Context
        fun getAppContext(): Context = DHApplication.context
    }

    override fun onCreate() {
        super.onCreate()

        Log.d("DHApplication onCreate()")

        DHApplication.context = applicationContext

        Thread.setDefaultUncaughtExceptionHandler { t, e ->
            Log.e("setDefaultUncaughtExceptionHandler()")
            Log.e(e.stackTraceToString())

            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            )
            exitProcess(0)
//            android.util.Log.getStackTraceString()
        }
    }
}