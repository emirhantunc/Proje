package com.example.animeproje

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.TimePicker
import android.widget.Toast
import com.example.animeproje.util.Loading
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_dogum_tarihi.*
import kotlinx.android.synthetic.main.activity_post_ekleme_a_ct.*
import kotlinx.android.synthetic.main.activity_profil__duzenle_a_c_t.*
import kotlinx.android.synthetic.main.fragment_kesfet_post.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.fragment_register.view.*
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap

class Profil_DuzenleACT : BaseActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var credential: AuthCredential
    private var filepath: Uri? = null
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
        setContentView(R.layout.activity_profil__duzenle_a_c_t)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        pickDate()
        image.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
        cinsiyet_duzenle.setOnClickListener {
            var popupmenucinsiyet = PopupMenu(this, cinsiyet_duzenle)
            popupmenucinsiyet.inflate(R.menu.popupcinsiyet)
            popupmenucinsiyet.setOnMenuItemClickListener {
                if (it.title=="Erkek"){
                   cinsiyet_duzenle.text="erkek"
                    val img = this@Profil_DuzenleACT.resources.getDrawable(R.drawable.erkek)
                    img.setBounds(0, 0, 60, 60)
                    cinsiyet_duzenle.setCompoundDrawables(img, null, null, null)
                }
                else{
                    cinsiyet_duzenle.text="kadın"
                    val img = this@Profil_DuzenleACT.resources.getDrawable(R.drawable.kadin_icon)
                    img.setBounds(0, 0, 60, 60)
                    cinsiyet_duzenle.setCompoundDrawables(img, null, null, null)
                }
                Toast.makeText(this, "Cinsiyet:" + it.title, Toast.LENGTH_LONG).show()
                true
            }
            popupmenucinsiyet.show()

        }




        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    var ismi = p0.child("adi").getValue(String::class.java)
                    var soyismi = p0.child("soyadi").getValue(String::class.java)
                    var birthday = p0.child("birthday").getValue(String::class.java)
                    var profileimage = p0.child("profilimage").getValue(String::class.java)
                    var cinsiyet = p0.child("cinsiyet").getValue(String::class.java)
                    var biyografi = p0.child("biyografi").getValue(String::class.java)
                    if (cinsiyet == "erkek") {
                        val img = this@Profil_DuzenleACT.resources.getDrawable(R.drawable.erkek)
                        img.setBounds(0, 0, 60, 60)
                        cinsiyet_duzenle.setCompoundDrawables(img, null, null, null)
                    } else {
                        val img = this@Profil_DuzenleACT.resources.getDrawable(R.drawable.kadin_icon)
                        img.setBounds(0, 0, 60, 60)
                        cinsiyet_duzenle.setCompoundDrawables(img, null, null, null)
                    }
                    degistir_isim.setText(ismi)
                    degistir_soyisim.setText(soyismi)
                    cinsiyet_duzenle.text = cinsiyet

                    dogumtarihi_textview.text = birthday
                    if (biyografi != null) {
                        if (biyografi.isNotEmpty()) {
                            biyografi_duzenle.setText(biyografi)
                        }
                    }
                    if (profileimage.isNullOrEmpty()) {
                        if (cinsiyet == "erkek") {
                            image.setBackgroundResource(R.drawable.icons8)
                        } else {
                            image.setBackgroundResource(R.drawable.ic_kadin_resim)
                        }
                    } else {
                        imageclass.imgload(this@Profil_DuzenleACT, profileimage ?: "", image)
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })





        onaylabutton.setOnClickListener {
            val sifre = degistir_Sifre.text.toString()
            val isim = degistir_isim.text.toString()
            val soyisim = degistir_soyisim.text.toString()
            if (sifre.isNullOrEmpty()) {
                hazirmsj("Şifrenizi Doğrulamanız Gerekmektedir")
            } else {
                credential = EmailAuthProvider.getCredential(
                    auth.currentUser?.email!!,
                    degistir_Sifre.text.toString()
                )
                showLoading()
                auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {

                    if (it.isSuccessful) {
                        if (isim.isNotEmpty() && soyisim.isNotEmpty()) {
                            duzenle()
                        }

                        if (filepath != null) {
                            hazirmsj("Profil Resminizin Güncellenmesi Birkaç Dakika Sürebilir")
                            uploadFile()
                        }
                    } else {
                        hazirmsj("Yanlış Şifre")
                    }
                    hideLoading()
                }
            }


        }
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
        dogumtarihi_textview.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this!!, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        savedDay = dayOfMonth
        savedMoth = month + 1
        savedYear = year
        val yas = dogumtarihi_textview.text.toString()
        if (savedMoth != 10 && savedMoth != 11 && savedMoth != 12) {
            birthday = "$savedDay-0$savedMoth-$savedYear"
        } else {
            birthday = "$savedDay-$savedMoth-$savedYear"

        }
        dogumtarihi_textview.text = birthday
        getDateTimeCalendar()
        if (yas.isNullOrEmpty()) {
            dogumtarihi_textview.visibility = View.GONE
        } else {
            dogumtarihi_textview.visibility = View.VISIBLE
        }
        // TimePickerDialog(context, this, hour, minute, true).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        savedHour = hourOfDay
        savedMinute = minute
    }

    private fun uploadFile() {
        if (filepath != null) {
            /*var pd = ProgressDialog(this)
            pd.setTitle("Yükleniyor")
            pd.setCanceledOnTouchOutside(false)
            pd.show()*/
            var profilimage = getRandomId() + ".jpg"
            var imagePref =
                FirebaseStorage.getInstance().reference.child("images").child(profilimage)
            imagePref.putFile(filepath!!).addOnSuccessListener { p0 ->
                p0.storage.downloadUrl.addOnCompleteListener {
                    if (it.isSuccessful && it.result != null) {
                        val url = it.result.toString()
                        val user = auth.currentUser
                        /*Güncelleme işlemi*/
                        val ref = database.child("users").child(user?.uid!!).child("profilimage")

                        ref.setValue(url).addOnCompleteListener { post ->
                            if (post.isSuccessful) {
                                Toast.makeText(application, "İşlem Başarılı", Toast.LENGTH_LONG)
                                    .show()
                                // pd.dismiss()

                            } else {
                                hazirmsj("Bir Hata Oluştu")
                            }
                            //pd.dismiss()
                        }
                    }
                }
            }
                .addOnFailureListener { p0 ->
                    //pd.dismiss()
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
                }.addOnProgressListener { p0 ->
                    /*var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Kalan${progress.toInt()}%")*/
                }
        }
    }


    private fun pickImageFromGallery() {

        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    private fun getRandomId() = UUID.randomUUID().toString()

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    pickImageFromGallery()
                } else {
                    hazirmsj("İzin Verilmedi")
                }
            }
        }
    }  //Galeri İzni İsteme kodu

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            image.setImageURI(data?.data)
            filepath = data?.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            image.setImageBitmap(bitmap)
        }
    }

    fun duzenle() {
        val isim = degistir_isim.text.toString()
        val soyisim = degistir_soyisim.text.toString()
        val biyografi = biyografi_duzenle.text.toString()
        val birthday = dogumtarihi_textview.text
        val cinsiyet=cinsiyet_duzenle.text
        val user = auth.currentUser
        val ref = database.child("users").child(user!!.uid)
        ref.child("adi").setValue(isim)
        ref.child("biyografi").setValue(biyografi)
        ref.child("birthday").setValue(birthday)
        ref.child("cinsiyet").setValue(cinsiyet)
        ref.child("soyadi").setValue(soyisim).addOnCompleteListener {
            if (it.isSuccessful) {
                onBackPressed()
            }
        }
    }

}


