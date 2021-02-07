package com.example.animeproje.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.animeproje.R
import com.example.animeproje.imageclass
import com.example.animeproje.imageclass2
import com.example.animeproje.imageclasserkek
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.*
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.view.*
import kotlinx.android.synthetic.main.fragment_hesap__goruntule_.view.profil_yas
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
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        root = inflater.inflate(R.layout.fragment_hesap__goruntule_, container, false)
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
                val nullresim = ""
                root.hesapgoruntule_isim.text = isim + " " + soyisim
                if (profilresim.isNullOrEmpty()) {
                    if (cinsiyet == "erkek") {
                        imageclasserkek.imgload2(context!!, nullresim, hesapgoruntule_profile_resim)
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

            override fun onCancelled(p0: DatabaseError) {
                hazirmsj("Bir Sorun Meydana Geldi,Tekrar Deneyiniz")
            }
        })



        return root
    }

}
