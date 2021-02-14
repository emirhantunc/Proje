package com.example.animeproje

import android.app.Dialog
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.animeproje.util.Loading

open class BaseActivity :AppCompatActivity(){
    private lateinit var loading: Dialog
    fun hazirmsj(mesaj: String) {
        Toast.makeText(this, mesaj, Toast.LENGTH_LONG).show();
    }
    fun showLoading() {
        if (!this::loading.isInitialized) {
            loading = Loading.showLoadingDialog(this)
        } else {
            if (!loading.isShowing) {
                loading.show()
            }
        }
    }

    fun hideLoading() {
        if (this::loading.isInitialized) {
            loading.dismiss()
            loading.cancel()
        }
    }


}