package com.example.animeproje.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.rangeTo

import com.example.animeproje.R
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sifre_degistir.*
import kotlinx.android.synthetic.main.fragment_sifre_degistir.view.*

/**
 * A simple [Fragment] subclass.
 */
class SifreDegistirFragment : BaseFragment() {
    lateinit var root: View
    private lateinit var database: DatabaseReference
    lateinit var credential: AuthCredential
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_sifre_degistir, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        root.sifredegistironayla.setOnClickListener {
            degistir()
        }
        return root
    }
    fun degistir(){
        val eskisifre = root.eski_sifre.text.toString()
        val yensifre = root.yeni_sifre.text.toString()
        val tekrar = root.yeni_sifre_tekrar.text.toString()
        val user = auth.currentUser

        credential = EmailAuthProvider.getCredential(
            auth.currentUser?.email!!,
            eski_sifre.text.toString()
        )
        auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener {

            if (eskisifre.isEmpty()) {
                root.eski_sifre.error = "Eski Şifrenizi Tekrar Girmeniz Gerekmektedir"
                root.eski_sifre.requestFocus()
            }
            if (yensifre.isEmpty()) {
                root.yeni_sifre.error = "Yeni Şifrenizi Şifrenizi Girmediniz"
                root.yeni_sifre.requestFocus()
            }
            if (tekrar.isEmpty()) {
                root.yeni_sifre_tekrar.error = "Yeni Şifrenizi Tekrar Girmeniz Gerekmektedir"
                root.yeni_sifre_tekrar.requestFocus()
            }
            if (tekrar != yensifre) {
                hazirmsj("Şifreler Uyuşmuyor")
            }
            if (yensifre.length<6){
                hazirmsj("Şifre Uzunluğu En Az 6 Haneli Olmalıdır")
            }

            showLoading()
            if (it.isSuccessful) {
                user?.updatePassword(yensifre)?.addOnCompleteListener {
                    if (it.isSuccessful) {
                        hazirmsj("Başarılı")
                        sayfadegistir(ProfileFragment())
                    } else {
                        hazirmsj("Başarısız")
                    }
                    hideLoading()
                }
            }
        }


    }


}
