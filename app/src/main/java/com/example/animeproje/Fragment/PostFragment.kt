package com.example.animeproje.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animeproje.Adapter.PostAdapter
import com.example.animeproje.R
import com.example.animeproje.model.post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PostFragment : BaseFragment() {
    private lateinit var database: DatabaseReference
    lateinit var root: View
    var profilresim = "https://i.pinimg.com/564x/a0/32/3d/a0323dafc53ed0e6bdd7a05e195d9f7b.jpg"
    var postresim =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbZVG9CtSy0qS7cruaI_0kIXybXnn31e8ViQ&usqp=CAU"
    var posttresim = "https://serdara.com/wp-content/uploads/tokyo-ghoul.png"


    var postList: ArrayList<post> = ArrayList()

    lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        root = inflater.inflate(R.layout.fragment_post, container, false)
        val ref = database.child("post")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    p0.children.forEach { userPost ->

                        val userId = userPost.key.toString()
                        val user = auth.currentUser
                        val userRef = database.child("users").child(userId)

                        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {

                                var username =
                                    p0.child("adi").value.toString() + " " + p0.child("soyadi").value.toString()

                                userPost.children.forEach { post ->
                                    val deneme = post.child("icerik").value.toString()
                                    val imageUrl = post.child("imageUrl").value.toString()
                                    postList.clear()
                                    postList.add(
                                        post(username, "24.01.2021", imageUrl, profilresim, deneme)
                                    )
                                }
                                if (postList.isEmpty()) {
                                    root.textviewww.visibility = View.VISIBLE
                                } else {
                                    root.textviewww.visibility = View.GONE
                                }
                                adapter.notifyDataSetChanged()
                            }

                            override fun onCancelled(p0: DatabaseError) {

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
        adapter = PostAdapter(postList, object : postinterface {
            override fun clickitem(a: post, b: Int) {

            }

            override fun clickpost(a: post) {
                val resimlink = a.postresim
                val bundle = Bundle()
                bundle.putString("profilimage", resimlink)
                val goruntuleFragment = GoruntuleFragment()
                goruntuleFragment.arguments = bundle
                sayfadegistir(goruntuleFragment)
            }
        })
        root.recy.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        root.recy.adapter = adapter
        return root
    }

    // fun getDate(time: Long): String {
    // val cal = Calendar.getInstance()
    //val tz = cal.timeZone
    //val sdf = SimpleDateFormat("dd-MM-yyyy")
    //sdf.timeZone = tz
    //val localTime=sdf.for
    //}


}

interface postinterface {
    fun clickitem(a: post, b: Int)
    fun clickpost(a: post)
}

