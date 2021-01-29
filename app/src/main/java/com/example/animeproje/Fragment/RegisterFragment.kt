package com.example.animeproje.Fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import com.example.animeproje.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.util.*
import kotlin.system.measureNanoTime


/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : BaseFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
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

    private lateinit var database: DatabaseReference
    lateinit var root: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_register, container, false)
        pickDate()
        database = FirebaseDatabase.getInstance().reference

        root.registerbutton.setOnClickListener {
            if (isNetworkAvailable(context!!)) {
                singupuser()
            } else {
                alertdialog()
            }
        }
        return root
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
        root.yas_button.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(context!!, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMoth = month + 1
        savedYear = year
        birthday = "$savedDay-$savedMoth-$savedYear"
        root.yas_view.text = birthday
        getDateTimeCalendar()
        // TimePickerDialog(context, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
    }


    fun singupuser() {

        val mail = root.registeremail.text.toString()
        val sifre = root.registersifre.text.toString()
        val sifretekrar = root.registersifretekrar.text.toString()
        val isim = root.isim_edittext.text.toString()
        val soyisim = root.soyisim_edittext.text.toString()


        when {
            isim.isEmpty() -> {
                root.isim_edittext.error = "İsim Boş Bırakılamaz"
                root.isim_edittext.requestFocus()
                return
            }
            soyisim.isEmpty() -> {
                root.soyisim_edittext.error = "Soyisim Boş Bırakılamaz"
                root.soyisim_edittext.requestFocus()
                return
            }
            mail.isEmpty() -> {
                root.registeremail.error = "Email Boş Bırakılamaz"
                root.registeremail.requestFocus()
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(mail).matches() -> {
                root.registeremail.error = "Lütfen E-mail giriniz!"
                root.registeremail.requestFocus()
                return
            }
            sifre.isEmpty() -> {
                root.registersifre.error = "Şifre Boş Bırakılamaz"
                root.registersifre.requestFocus()
                return
            }
            sifretekrar.isEmpty() -> {
                root.registersifretekrar.error = "Şifrenizi Tekrar Girmeniz Gerekmektedir"
                root.registersifretekrar.requestFocus()
                return
            }
            sifretekrar != sifre -> {
                root.registersifretekrar.error = "Şifreler Uyuşmuyor"
                return
            }
            sifre.length < 6 -> {
                hazirmsj("Şifre en az 6 haneli olmak zorundadır")
            }
        }

        showLoading()
        auth.createUserWithEmailAndPassword(mail, sifre).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.sendEmailVerification()?.addOnCompleteListener { tast ->
                    if (tast.isSuccessful) {
                        hazirmsj("Başarılı,E-mailinizi Kontrol Ediniz")
                        val ref = database.child("users").child(user.uid)
                        val map = hashMapOf("adi" to isim, "soyadi" to soyisim, "yasi" to birthday)
                        ref.setValue(map).addOnCompleteListener {
                            if (it.isSuccessful) {
                                sayfadegistir(LoginFragment())
                            } else {
                                hazirmsj("Kayıt Olma Başarısız")
                            }
                        }
                    }
                }
            } else {
                hazirmsj("Kayıt Olma Başarısız")
            }
            hideLoading()
        }
    }

    @SuppressLint("MissingPermission")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    fun alertdialog() {
        val builder = AlertDialog.Builder(context!!)
        //set title for alert dialog
        builder.setTitle("İnternet Bağlantınızı Kontrol Ediniz!")
        //set message for alert dialog
        builder.setMessage("Tekrar Denemek İster misiniz?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)


        //performing positive action
        builder.setPositiveButton("Evet") { dialogInterface, which ->

        }
        //performing cancel action
        //performing negative action
        builder.setNegativeButton("Hayır") { dialogInterface, which ->
            getMainActivity().finish()
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}


