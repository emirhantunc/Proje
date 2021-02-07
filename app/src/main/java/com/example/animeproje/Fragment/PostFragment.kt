package com.example.animeproje.Fragment

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.animeproje.Adapter.PostAdapter
import com.example.animeproje.Adapter.PostTabLayout
import com.example.animeproje.Adapter.ProfileTabLayout
import com.example.animeproje.R
import com.example.animeproje.R.color.mor
import com.example.animeproje.model.post
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_kesfet_post.view.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*
import kotlinx.android.synthetic.main.fragment_post.viewPager
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kotlinx.android.synthetic.main.item.*
import kotlinx.android.synthetic.main.item.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.android.synthetic.main.fragment_post.view.viewPager as viewPager1


class PostFragment : BaseFragment() {
    private lateinit var database: DatabaseReference
    lateinit var root: View
    var postList: ArrayList<post> = ArrayList()

    lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        database = FirebaseDatabase.getInstance().reference
        root = inflater.inflate(R.layout.fragment_post, container, false)

        root.tablayout_post.apply {
            addTab(newTab().setText("Takip Edilen"))
            addTab(newTab().setText("Sizin İçin"))
            tabGravity = TabLayout.GRAVITY_FILL
        }
        val adapter = PostTabLayout(context!!,childFragmentManager, root.tablayout_post!!.tabCount)
        root.viewPager!!.adapter = adapter

        root.viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(root.tablayout_post))

        root.tablayout_post!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {

            }
            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
        return root
    }

    /* fun getDate(time: Long): String {
     val cal = Calendar.getInstance()
    val tz = cal.timeZone
    val sdf = SimpleDateFormat("dd-MM-yyyy")
    sdf.timeZone = tz
    val localTime=sdf.for
    }
    */

}




