package com.example.animeproje

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_post_ekleme_a_ct.*
import kotlinx.android.synthetic.main.activity_profil__duzenle_a_c_t.*
import java.util.*
import java.util.jar.Manifest
import kotlin.collections.HashMap

class Profil_DuzenleACT : BaseActivity() {
    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    lateinit var credential: AuthCredential
    private var filepath: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil__duzenle_a_c_t)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

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

        onaylabutton.setOnClickListener {
            val sifre = degistir_Sifre.text.toString()
            val isim=degistir_isim.text.toString()
            val soyisim=degistir_soyisim.text.toString()
            if (sifre.isNullOrEmpty()) {
                hazirmsj("Şifrenizi Doğrulamanız Gerekmektedir")
            } else {
                credential = EmailAuthProvider.getCredential(
                    auth.currentUser?.email!!,
                    degistir_Sifre.text.toString()
                )
                auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {

                    if (it.isSuccessful) {
                        if (isim.isNotEmpty()&&soyisim.isNotEmpty()){
                            duzenle()
                        }
                        if (filepath != null) {
                            uploadFile()
                        }
                    }
                    else {
                        hazirmsj("Yanlış Şifre")
                    }
                }
            }


        }
    }

    private fun uploadFile() {
        if (filepath != null) {
            var pd = ProgressDialog(this)
            pd.setTitle("Yükleniyor")
            pd.setCanceledOnTouchOutside(false)
            pd.show()
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
                                onBackPressed()
                            } else {
                                hazirmsj("Bir Hata Oluştu")
                            }
                            pd.dismiss()
                        }
                    }
                }
            }
                .addOnFailureListener { p0 ->
                    pd.dismiss()
                    Toast.makeText(applicationContext, p0.message, Toast.LENGTH_LONG).show()
                }.addOnProgressListener { p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Kalan${progress.toInt()}%")
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
            ımage.setImageURI(data?.data)
            filepath = data?.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            ımage.setImageBitmap(bitmap)
        }
    }

    fun duzenle() {
        val isim=degistir_isim.text.toString()
        val soyisim=degistir_soyisim.text.toString()
        val user=auth.currentUser
        val ref=database.child("users").child(user!!.uid)
        ref.child("adi").setValue(isim)
        ref.child("soyadi").setValue(soyisim).addOnCompleteListener {
            if(it.isSuccessful){
                onBackPressed()
            }
        }


    }
}


