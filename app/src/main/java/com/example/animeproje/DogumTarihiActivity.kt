package com.example.animeproje

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_dogum_tarihi.*
import kotlinx.android.synthetic.main.activity_profil__duzenle_a_c_t.*
import java.util.*

class DogumTarihiActivity : BaseActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var root: View
    lateinit var credential: AuthCredential
    var birthday = ""
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMoth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dogum_tarihi)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        pickDate()
        onayla_dogumtarihi.setOnClickListener {
            buttonfun()
        }
        val user=auth.currentUser
        val ref=database.child("users").child(user!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    var birthday=p0.child("birthday").getValue(String::class.java)
                    dogumyasi.text=birthday
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }


    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun pickDate() {
        dogumtarihi_button.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this!!, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMoth = month + 1
        savedYear = year
        val yas = dogumyasi.text.toString()
        if (savedMoth != 10 && savedMoth != 11 && savedMoth != 12) {
            birthday = "$savedDay-0$savedMoth-$savedYear"
        } else {
            birthday = "$savedDay-$savedMoth-$savedYear"

        }
        dogumyasi.text = birthday
        getDateTimeCalendar()
        if (yas.isNullOrEmpty()) {
            dogumyasi.visibility = View.GONE
        } else {
            dogumyasi.visibility = View.VISIBLE
        }
        // TimePickerDialog(context, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
    }

    fun duzenle() {
        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        ref.child("birthday").setValue(birthday).addOnCompleteListener {
            if (it.isSuccessful) {
                MainActivity.openMainact(this)
            } else {
                hazirmsj("Bir Hata Oluştu")
            }
        }
    }

    fun buttonfun() {
        val sifre = dogumtarihi_sifre.text.toString()
        if (birthday.isEmpty()) {
           hazirmsj("Doğum Tarihinizi Giriniz")
        }
        when {
            sifre.isEmpty() -> {
                hazirmsj("Şifrenizi Giriniz!")

            }
            birthday.isNullOrEmpty() -> {
                hazirmsj("Doğum Tarihinizi Girmediniz")
            }
            else -> {
                showLoading()
                credential = EmailAuthProvider.getCredential(
                    auth.currentUser?.email!!,
                    dogumtarihi_sifre.text.toString()
                )
                auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {

                    if (it.isSuccessful) {
                        duzenle()

                    } else {
                        hazirmsj("Yanlış Şifre")
                    }
                    hideLoading()
                }

            }

        }

    }

    companion object {

        fun openactivity(context: Context) {
            val intent = Intent(context, DogumTarihiActivity::class.java)
            context.startActivity(intent)
        }
    }
}
