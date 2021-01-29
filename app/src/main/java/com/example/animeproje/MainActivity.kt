package com.example.animeproje

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.animeproje.Fragment.HomeFragment
import com.example.animeproje.Fragment.LoginFragment
import com.google.firebase.auth.FirebaseAuth
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val loginFragment = LoginFragment()
            openFragment(loginFragment)
        } else {
            val homeFragment = HomeFragment()
            openFragment(homeFragment)
        }


    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.container, fragment).commit()
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }

    }


    companion object {
        fun openMainact(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}
