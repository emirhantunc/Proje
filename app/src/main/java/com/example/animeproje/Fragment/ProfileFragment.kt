package com.example.animeproje.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.viewpager.widget.ViewPager
import com.example.animeproje.*
import com.example.animeproje.Adapter.ProfileTabLayout
import com.example.animeproje.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment() {
    lateinit var root: View
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)


        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)


        root.tablayout.apply {
            addTab(newTab().setIcon(R.drawable.ic_home))
            addTab(newTab().setIcon(R.drawable.ic_favorite_))
            tabGravity = TabLayout.GRAVITY_FILL
            repeat(tabCount) { i ->
                getTabAt(i)?.icon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            }
        }
        val adapter = ProfileTabLayout(context!!, childFragmentManager, root.tablayout!!.tabCount)
        root.viewPager!!.adapter = adapter

        root.viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(root.tablayout))

        root.tablayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })




        database = FirebaseDatabase.getInstance().reference
        showLoading()
        root.settings_button.setOnClickListener {
            sayfadegistir(SettingsFragment())
        }
        root.düzenle.setOnClickListener {
            val intent = Intent(context, Profil_DuzenleACT::class.java)
            startActivity(intent)
        }
        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val isim = p0.child("adi").getValue(String::class.java)
                    val soyisim = p0.child("soyadi").getValue(String::class.java)
                    val birthday = p0.child("birthday").getValue(String::class.java)
                    val cinsiyet = p0.child("cinsiyet").getValue(String::class.java)
                    val profilresim = p0.child("profilimage").getValue(String::class.java)
                    val biyografi = p0.child("biyografi").getValue(String::class.java)
                    val nullresim = ""
                    root.profile_name.text = isim + " " + soyisim
                    root.biyografi_profile.text = biyografi
                    if (profilresim != null) {
                        imageclass.imgload(context!!, profilresim ?: "", root.profile_resim)
                    } else {
                        if (cinsiyet == "kadın")
                            imageclass2.imgload2(context!!, nullresim, root.profile_resim)
                        if (cinsiyet == "erkek") {
                            imageclasserkek.imgload2(context!!, nullresim, root.profile_resim)
                        }
                    }


                    //Profil Resmini Firebaseden çekme
                    if (cinsiyet == "erkek") {
                        val img = context!!.resources.getDrawable(R.drawable.erkek)
                        img.setBounds(0, 0, 60, 60)
                        root.profil_yas.setCompoundDrawables(img, null, null, null)
                    } else if (cinsiyet == "kadın") {
                        val img = context!!.resources.getDrawable(R.drawable.kadin_icon)
                        img.setBounds(0, 0, 60, 60)
                        root.profil_yas.setCompoundDrawables(img, null, null, null)
                    }
                    if (!birthday.isNullOrEmpty()) {
                        root.profil_yas.text = birthday
                        root.profil_yas.visibility = View.VISIBLE
                    } else {
                        root.profil_yas.visibility = View.GONE
                    }
                    hideLoading()
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            fun int() {
                R.drawable.icons8
                R.drawable.ic_kadin_resim
            }
        })
        return root
    }


}
