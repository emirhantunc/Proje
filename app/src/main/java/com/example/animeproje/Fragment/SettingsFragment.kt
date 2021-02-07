package com.example.animeproje.Fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.animeproje.R
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment() {
    lateinit var root: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_settings, container, false)
        root.hesap_cikis_button.setOnClickListener {
            alertdialog()
        }
        root.Sifre_degistir.setOnClickListener {
            sifreDegistirFragment()
        }
        return root
    }


    fun sifreDegistirFragment() {

        sayfadegistir(SifreDegistirFragment())

    }

    fun alertdialog() {
        val builder = AlertDialog.Builder(context)
        //set title for alert dialog
        builder.setTitle("Hesaptan Çıkıyorsunuz")
        //set message for alert dialog
        builder.setMessage("Hesaptan Çıkmak İstediğinize Emin misiniz?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)


        //performing positive action
        builder.setPositiveButton("Evet") { dialogInterface, which ->
            sayfadegistir(LoginFragment())
            auth.signOut()
        }
        //performing cancel action
        //performing negative action
        builder.setNegativeButton("Hayır") { dialogInterface, which ->

        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}
