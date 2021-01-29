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
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.fragment_post.view.*


class PostFragment : BaseFragment() {
    lateinit var root: View
    var profilresim = "https://i.pinimg.com/564x/a0/32/3d/a0323dafc53ed0e6bdd7a05e195d9f7b.jpg"
    var postresim =
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSbZVG9CtSy0qS7cruaI_0kIXybXnn31e8ViQ&usqp=CAU"
    var posttresim = "https://serdara.com/wp-content/uploads/tokyo-ghoul.png"
    var post = listOf(
        post("Emirhan", "24.01.2021", postresim, profilresim, "Deneme 1-2"),
        post("Kemal", "24.01.2021", posttresim, profilresim, "deneme 3-4"),
        post("Tuna", "24.01.2021", postresim, profilresim, "deneme 3-4"),
        post("Tunç", "24.01.2021", postresim, profilresim, "deneme 3-4")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_post, container, false)
        val adapter = PostAdapter(post, object : postinterface {
            override fun clickitem(a: post, b: Int) {

            }

            override fun clickpost(a: post) {
                val resimlink = a.postresim
                val bundle = Bundle()
                bundle.putString("profilimage", resimlink)
                val goruntuleFragment = GoruntuleFragment()
                goruntuleFragment.arguments=bundle
                sayfadegistir(goruntuleFragment)
            }
        })
        root.recy.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        root.recy.adapter = adapter
        return root
    }

}

interface postinterface {
    fun clickitem(a: post, b: Int)
    fun clickpost(a: post)
}

