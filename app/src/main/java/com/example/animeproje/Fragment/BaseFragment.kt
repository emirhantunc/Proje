package com.example.animeproje.Fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.animeproje.MainActivity
import com.example.animeproje.util.Loading
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

open class BaseFragment : Fragment() {
    private lateinit var loading: Dialog
    lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    fun hazirmsj(mesaj: String) {
        Toast.makeText(context, mesaj, Toast.LENGTH_LONG).show();
    }

    fun getMainActivity(): MainActivity {
        return (activity as MainActivity)
    }

    fun sayfadegistir(fragment: Fragment) {
        getMainActivity().openFragment(fragment)
    }

    fun showLoading() {
        if (!this::loading.isInitialized) {
            loading = Loading.showLoadingDialog(context!!)
        } else {
            if (!loading.isShowing) {
                loading.show()
            }
        }
    }

    fun hideLoading() {
        if (this::loading.isInitialized) {
            loading.dismiss()
            loading.cancel()
        }
    }
}

