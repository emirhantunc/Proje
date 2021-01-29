package com.example.animeproje.Fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import com.example.animeproje.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : BaseFragment(){

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
        root.settings_button.setOnClickListener {
            sayfadegistir(SettingsFragment())
        }
        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val isim = p0.child("adi").getValue(String::class.java)
                    val soyisim = p0.child("soyadi").getValue(String::class.java)
                    val yasi = p0.child("yasi").getValue(String::class.java)
                    root.profile_name.text = isim + " " + soyisim
                    root.profil_yas.text = yasi
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
        return root
    }







}
