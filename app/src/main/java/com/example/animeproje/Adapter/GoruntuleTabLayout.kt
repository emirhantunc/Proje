package com.example.animeproje.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.animeproje.Fragment.Fav_TabLayout
import com.example.animeproje.Fragment.Post_Tablayout

class GoruntuleTabLayout(
    private val myContext: Context,
    fm: FragmentManager,
    internal val totalTabs: Int
) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                Post_Tablayout()
            }
            1 -> {
                Fav_TabLayout()
            }
            else -> Post_Tablayout()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}