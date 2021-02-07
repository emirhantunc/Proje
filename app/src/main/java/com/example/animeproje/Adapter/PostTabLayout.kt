package com.example.animeproje.Adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.animeproje.Fragment.*

class PostTabLayout (
    private val myContext: Context,
    fm: FragmentManager,
    internal val totalTabs: Int
) : FragmentPagerAdapter(fm) {


    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                FallowPostFragment()
            }
            1 -> {
                KesfetPostFragment()
            }
            else -> KesfetPostFragment()
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}