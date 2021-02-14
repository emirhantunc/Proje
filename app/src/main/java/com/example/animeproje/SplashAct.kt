package com.example.animeproje

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash.*

class SplashAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        splashlogo.alpha=0f

        splashlogo.animate().setDuration(1500).alpha(1f).withEndAction{
            MainActivity.openMainact(this)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }

        /*Handler().postDelayed({
            MainActivity.openMainact(this)
            startActivity(intent)
        }, 2000)*/
    }
}
