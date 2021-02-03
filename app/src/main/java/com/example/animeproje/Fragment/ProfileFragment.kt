package com.example.animeproje.Fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import com.example.animeproje.Profil_DuzenleACT
import com.example.animeproje.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


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

        database = FirebaseDatabase.getInstance().reference
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_profile, container, false)
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
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val isim = p0.child("adi").getValue(String::class.java)
                    val soyisim = p0.child("soyadi").getValue(String::class.java)
                    val birthday = p0.child("birthday").getValue(String::class.java)
                    val cinsiyet = p0.child("cinsiyet").getValue(String::class.java)
                    root.profile_name.text = isim + " " + soyisim
                    if (cinsiyet == "erkek") {
                        val img = context!!.resources.getDrawable(R.drawable.erkek)
                        img.setBounds(0, 0, 60, 60)
                        root.profil_yas.setCompoundDrawables(img, null, null, null)
                    } else if (cinsiyet == "kadın") {
                        val img = context!!.resources.getDrawable(R.drawable.kadin)
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
        })
        return root
    }


}
