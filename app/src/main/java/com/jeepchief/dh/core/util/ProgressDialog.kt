package com.jeepchief.dh.core.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.jeepchief.dh.databinding.LayoutProgressDialogBinding

object ProgressDialog {
    private lateinit var dlg: AlertDialog
    fun showProgressDialog(context: Context) : AlertDialog {
        val dlgView = LayoutProgressDialogBinding.inflate(LayoutInflater.from(context))
        dlg = AlertDialog.Builder(context).create().apply {
            setView(dlgView.root)
            setCancelable(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        return dlg
    }

    fun dismissDialog() {
        try {
            dlg.dismiss()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }
}