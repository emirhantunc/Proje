package com.example.animeproje

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

open class BaseActivity :AppCompatActivity(){
    fun hazirmsj(mesaj: String) {
        Toast.makeText(this, mesaj, Toast.LENGTH_LONG).show();
    }
}