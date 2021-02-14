package com.example.animeproje

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_cinsiyet_duzenle.*
import kotlinx.android.synthetic.main.activity_profil__duzenle_a_c_t.*
import kotlinx.android.synthetic.main.fragment_register.*

class CinsiyetDuzenleActivity : BaseActivity() {
    var cinsiyet = ""
    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinsiyet_duzenle)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        cinsiyet_onayla.setOnClickListener {
            duzenle()
        }
        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    var cinsiyet = p0.child("cinsiyet").getValue(String::class.java)
                    when{
                        cinsiyet=="erkek"->{
                            radio_erkek_duzenle.isChecked=true

                        }
                        cinsiyet=="kadın"->{
                            radio_kadın_duzenle.isChecked=true
                        }

                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })

    }

    fun duzenle() {
        when {
            radio_erkek_duzenle.isChecked -> {
                cinsiyet = "erkek"
            }
            radio_kadın_duzenle.isChecked -> {
                cinsiyet = "kadın"
            }
            else -> {
                hazirmsj("Lütfen Bir Cinsiyet Belirtin!")
                return
            }
        }


        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        showLoading()
        ref.child("cinsiyet").setValue(cinsiyet).addOnCompleteListener {
            hideLoading()
            onBackPressed()
        }

    }
}
