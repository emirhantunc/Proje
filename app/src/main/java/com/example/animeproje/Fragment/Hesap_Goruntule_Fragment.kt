package com.example.animeproje.Fragment

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animeproje.*
import com.example.animeproje.Adapter.ProfileTabLayout
import com.example.animeproje.R

import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.*
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.profil_yas
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.view.*
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.view.profil_yas
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.mesajitem.*

/**
 * A simple [Fragment] subclass.
 */
class Hesap_Goruntule_Fragment : BaseFragment() {
    lateinit var root: View
    private lateinit var database: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        root = inflater.inflate(R.layout.fragment_hesap__goruntule_, container, false)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        root.hesapgoruntule_settings.setOnClickListener {
            sayfadegistir(SettingsFragment())
        }
        root.hesapgoruntule_duzenle.setOnClickListener {
            val intent = Intent(context, Profil_DuzenleACT::class.java)
            startActivity(intent)
        }
        root.tablayout_goruntule.apply {
            addTab(newTab().setIcon(R.drawable.ic_home))
            addTab(newTab().setIcon(R.drawable.ic_favorite_))
            tabGravity = TabLayout.GRAVITY_FILL
            repeat(tabCount) { i ->
                getTabAt(i)?.icon?.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN)
            }
        }
        val adapter =
            ProfileTabLayout(context!!, childFragmentManager, root.tablayout_goruntule!!.tabCount)
        root.viewPager_goruntule!!.adapter = adapter
        root.viewPager_goruntule!!.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                root.tablayout_goruntule
            )
        )

        root.tablayout_goruntule!!.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager_goruntule!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        val user = auth.currentUser
        val userId = arguments?.getString("profile")
        var ref = database.child("users").child(userId!!)
        showLoading()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val isim = p0.child("adi").getValue(String::class.java)
                val soyisim = p0.child("soyadi").getValue(String::class.java)
                val birthday = p0.child("birthday").getValue(String::class.java)
                val cinsiyet = p0.child("cinsiyet").getValue(String::class.java)
                val profilresim = p0.child("profilimage").getValue(String::class.java)
                val biyografi = p0.child("biyografi").getValue(String::class.java)

                val nullresim = ""
                if (userId == user?.uid) {
                    hesapgoruntule_duzenle.visibility = View.VISIBLE
                    hesapgoruntule_settings.visibility = View.VISIBLE
                    takip_et.visibility = View.GONE
                    mesajgonder.visibility = View.GONE
                    root.hesapgoruntule_isim.text = isim + " " + soyisim
                    root.hesapgoruntulebiyografi.text=biyografi
                    if (profilresim.isNullOrEmpty()) {
                        if (cinsiyet == "erkek") {
                            imageclasserkek.imgload2(
                                context!!,
                                nullresim,
                                hesapgoruntule_profile_resim
                            )
                        } else {
                            imageclass2.imgload2(context!!, nullresim, hesapgoruntule_profile_resim)
                        }
                    } else {
                        imageclass.imgload(context!!, profilresim, hesapgoruntule_profile_resim)
                    }
                    if (birthday.isNullOrEmpty()) {
                        profil_yas.visibility = View.GONE
                    } else {
                        profil_yas.visibility = View.VISIBLE
                        profil_yas.text = birthday
                        if (cinsiyet == "erkek") {
                            val img = context!!.resources.getDrawable(R.drawable.erkek)
                            img.setBounds(0, 0, 60, 60)
                            root.profil_yas.setCompoundDrawables(img, null, null, null)
                        } else {
                            val img = context!!.resources.getDrawable(R.drawable.kadin_icon)
                            img.setBounds(0, 0, 60, 60)
                            root.profil_yas.setCompoundDrawables(img, null, null, null)
                        }
                    }
                    hideLoading()
                } else {
                    root.hesapgoruntule_isim.text = isim + " " + soyisim
                    root.hesapgoruntulebiyografi.text=biyografi
                    if (profilresim.isNullOrEmpty()) {
                        if (cinsiyet == "erkek") {
                            imageclasserkek.imgload2(
                                context!!,
                                nullresim,
                                hesapgoruntule_profile_resim
                            )
                        } else {
                            imageclass2.imgload2(context!!, nullresim, hesapgoruntule_profile_resim)
                        }
                    } else {
                        imageclass.imgload(context!!, profilresim, hesapgoruntule_profile_resim)
                    }
                    if (birthday.isNullOrEmpty()) {
                        profil_yas.visibility = View.GONE
                    } else {
                        profil_yas.visibility = View.VISIBLE
                        profil_yas.text = birthday
                        if (cinsiyet == "erkek") {
                            val img = context!!.resources.getDrawable(R.drawable.erkek)
                            img.setBounds(0, 0, 60, 60)
                            root.profil_yas.setCompoundDrawables(img, null, null, null)
                        } else {
                            val img = context!!.resources.getDrawable(R.drawable.kadin_icon)
                            img.setBounds(0, 0, 60, 60)
                            root.profil_yas.setCompoundDrawables(img, null, null, null)
                        }
                    }
                    hideLoading()
                }
            }


            override fun onCancelled(p0: DatabaseError) {
                hazirmsj("Bir Sorun Meydana Geldi,Tekrar Deneyiniz")
            }
        })



        return root
    }

}
