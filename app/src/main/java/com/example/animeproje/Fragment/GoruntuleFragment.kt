package com.example.animeproje.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.animeproje.R
import com.example.animeproje.imageclass
import kotlinx.android.synthetic.main.fragment_goruntule.view.*
import kotlinx.android.synthetic.main.mesajitem.view.*


class GoruntuleFragment : BaseFragment() {
    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_goruntule, container, false)
        val resimlink = arguments?.getString("profilimage")
        imageclass.imgload(context!!, resimlink!!, root.goruntule)
        return root

    }

}
