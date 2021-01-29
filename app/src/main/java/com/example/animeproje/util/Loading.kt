package com.example.animeproje.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.example.animeproje.R
import kotlinx.android.synthetic.main.loading.*

object Loading {
    fun showLoadingDialog(context: Context): Dialog {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.loading)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()


        return dialog
    }
}