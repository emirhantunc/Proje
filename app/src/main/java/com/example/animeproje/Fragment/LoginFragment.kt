package com.example.animeproje.Fragment

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.animeproje.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_login.view.*
import kotlinx.android.synthetic.main.fragment_register.view.*


/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : BaseFragment() {

    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_login, container, false)
        root.loginkayıtol.setOnClickListener {
            goregister()
        }
        root.logingirisyap.setOnClickListener {
            if (isNetworkAvailable(context!!)){
                doLogin()
            }else{
                alertdialog()
            }
        }



        return root

    }

    fun goregister() {
            sayfadegistir(RegisterFragment())
    }


    private fun doLogin() {
        val email = root.loginkulad.text.toString()
        val sifre = root.loginsifre.text.toString()
        when{
            email.isEmpty()->{
                root.loginkulad.error="E-mail Boş Bırakılamaz"
                root.loginkulad.requestFocus()
                return
            }
            !Patterns.EMAIL_ADDRESS.matcher(email).matches()->{
                root.loginkulad.error="Lütfen E-mail Giriniz"
                root.loginkulad.requestFocus()
                return
            }
            sifre.isEmpty()->{
                root.loginsifre.error="Şifre Boş Bırakılamaz"
                root.loginsifre.requestFocus()
                return
            }
        }
        showLoading()
        auth.signInWithEmailAndPassword(email, sifre).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    if (user.isEmailVerified) {
                        sayfadegistir(HomeFragment())
                    } else {
                        hazirmsj("Lütfen E-mail Adresinizi Doğrulayınız")
                    }

                }
            } else {
                hazirmsj("Kullanıcı Bilgileriniz Yanlış Olabilir")
            }
            hideLoading()
        }
    }
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
