package com.example.animeproje.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animeproje.Adapter.PostAdapter

import com.example.animeproje.R
import com.example.animeproje.model.post
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_kesfet_post.view.*
import kotlinx.android.synthetic.main.fragment_post.view.*

/**
 * A simple [Fragment] subclass.
 */
class KesfetPostFragment : BaseFragment() {
    private lateinit var database: DatabaseReference
    lateinit var root: View
    lateinit var adapter: PostAdapter
    var postList: ArrayList<post> = ArrayList()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_kesfet_post, container, false)
        database = FirebaseDatabase.getInstance().reference
        getPost()

        adapter = PostAdapter(postList, object : postinterface {
            override fun clickitem(a: post, b: Int) {

            }

            override fun clickpost(a: post) {

            }

            override fun clicklike(a: post) {

            }

            override fun clickprofile(a: post) {
                val user = a.userId
                val bundle = Bundle()
                bundle.putString("profile", user)
                val hesapGoruntule = Hesap_Goruntule_Fragment()
                hesapGoruntule.arguments = bundle
                sayfadegistir(hesapGoruntule)
            }


        })
        root.recy.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        root.recy.adapter = adapter
        return root
    }

    fun getPost() {
        val ref = database.child("post")
        postList.clear()
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    p0.children.forEach { post ->
                        val userId = post.child("userId").value.toString()
                        val deneme = post.child("icerik").value.toString()
                        val imageUrl = post.child("imageUrl").value.toString()
                        val tarih = post.child("tarih").value.toString()
                        val userRef = database.child("users").child(userId)
                        showLoading()


                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            @SuppressLint("ResourceAsColor")
                            override fun onDataChange(p0: DataSnapshot) {

                                var username =
                                    p0.child("adi").value.toString() + " " + p0.child("soyadi").value.toString()
                                var profilresim: String? = p0.child("profilimage").value.toString()
                                var cinsiyet = p0.child("cinsiyet").value.toString()
                                var nullprofilresim = ""
                                postList.add(
                                    post(
                                        username,
                                        tarih,
                                        imageUrl,
                                        profilresim,
                                        deneme,
                                        cinsiyet,
                                        userId
                                    )
                                )
                                if (postList.isEmpty()) {
                                    root.textviewww.visibility = View.VISIBLE
                                } else {
                                    root.textviewww.visibility = View.GONE

                                }
                                postList.sortByDescending {
                                    it.tarih
                                }

                                adapter.notifyDataSetChanged()
                                hideLoading()
                            }

                            override fun onCancelled(p0: DatabaseError) {
                                hazirmsj("Bir Sorun Meydana Geldi,Tekrar Deneyiniz")
                            }


                        })

                    }

                } else {
                    root.textviewww.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

}

interface postinterface {
    fun clickitem(a: post, b: Int)
    fun clickpost(a: post)
    fun clickprofile(a: post)
    fun clicklike(a: post)
}
