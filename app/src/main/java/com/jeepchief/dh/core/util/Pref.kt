package com.jeepchief.dh.core.util

import android.content.Context
import android.content.SharedPreferences
import com.jeepchief.dh.DHApplication

object Pref {
    private val PREF_NAME = "DH"
    private val preference: SharedPreferences by lazy { DHApplication.getAppContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE) }

    const val FIRST_LOGIN = "FIRST_LOGIN"
    const val CHARACTER_INFO = "CHARACTER_INFO"

    fun getString(id: String?) : String? {
        return preference.getString(id, "")
    }

    fun getBoolean(id: String?) : Boolean {
        return preference.getBoolean(id, false)
    }

    fun setValue(id: String?, value: String) : Boolean {
        return preference.edit()
            .putString(id, value)
            .commit()
    }

    fun setValue(id: String?, value: Boolean) : Boolean {
        return preference.edit()
            .putBoolean(id, value)
            .commit()
    }

    fun removeValue(id: String?) : Boolean {
        return preference.edit()
            .remove(id)
            .commit()
    }
}