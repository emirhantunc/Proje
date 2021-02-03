package com.example.animeproje

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.storage.FirebaseStorage
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_post_ekleme_a_ct.*
import java.util.*


class PostEklemeACt : BaseActivity() {

    private lateinit var database: DatabaseReference
    lateinit var auth: FirebaseAuth
    private var filepath: Uri? = null
    private val Image_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_ekleme_a_ct)
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()
        post_postimage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE)
                } else {
                    pickImageFromGallery()
                }
            } else {
                pickImageFromGallery()
            }
        }
        resim_button.setOnClickListener {
            if (filepath != null) {
                uploadFile()
            } else {
                hazirmsj("Resim Seçmediniz")

            }
        }
    }

    private fun uploadFile() {
        if (filepath != null) {
            var pd = ProgressDialog(this)
            pd.setTitle("Yükleniyor")
            pd.setCanceledOnTouchOutside(false)
            pd.show()

            var imageName = getRandomId() + ".jpg"

            var imagePref = FirebaseStorage.getInstance().reference.child("images").child(imageName)
            imagePref.putFile(filepath!!)
                .addOnSuccessListener { p0 ->
                    p0.storage.downloadUrl.addOnCompleteListener {
                        if (it.isSuccessful && it.result != null) {
                            val url = it.result.toString()
                            val aciklama = post_aciklama.text.toString()
                            val user = auth.currentUser
                            val ref = database.child("post").child(user?.uid!!).push()
                            val map = hashMapOf(
                                "imageUrl" to url,
                                "icerik" to aciklama,
                                "tarih" to ServerValue.TIMESTAMP
                            )
                            ref.setValue(map).addOnCompleteListener { post ->
                                if (post.isSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "İşlem Başarılı",
                                        Toast.LENGTH_LONG
                                    ).show()
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
                }
                .addOnProgressListener { p0 ->
                    var progress = (100.0 * p0.bytesTransferred) / p0.totalByteCount
                    pd.setMessage("Kalan ${progress.toInt()}%")
                }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, Image_PICK_CODE)
    }


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
                    hazirmsj("İzin Reddedildi")
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Image_PICK_CODE && data != null) {
            selected_image.setImageURI(data?.data)
            filepath = data.data!!
            var bitmap = MediaStore.Images.Media.getBitmap(contentResolver, filepath)
            selected_image.setImageBitmap(bitmap)
        }
    }

    private fun getRandomId() = UUID.randomUUID().toString()


    companion object {
        fun openPostEkleme(context: Context) {
            val intent = Intent(context, PostEklemeACt::class.java)
            context.startActivity(intent)
        }
    }
}
