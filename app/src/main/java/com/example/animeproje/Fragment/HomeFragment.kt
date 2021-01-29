package com.example.animeproje.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animeproje.PostEklemeACt

import com.example.animeproje.R
import kotlinx.android.synthetic.main.fragment_home.view.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment() {
    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_home, container, false)
        val profilefragment = ProfileFragment()
        val mesajFragment = MesajFragment()
        val postFragment=PostFragment()
        openFragment(postFragment)
        root.navigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.ProfileButton -> {
                    openFragment(profilefragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.mesaj -> {
                    openFragment(mesajFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.postbutton->{
                    openFragment(postFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.postekle->{
                    PostEklemeACt.openPostEkleme(context!!)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        return root
    }

    fun openFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().addToBackStack(null)
            .replace(R.id.frame, fragment).commit()
    }

}
