package com.example.animeproje.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.animeproje.Fragment.PostFragment
import com.example.animeproje.Fragment.postinterface
import com.example.animeproje.MainActivity
import com.example.animeproje.R
import com.example.animeproje.imageclass
import com.example.animeproje.model.post
import kotlinx.android.synthetic.main.item.view.*
import kotlin.coroutines.coroutineContext

class PostAdapter(
    val list: List<post>, val postinterface: postinterface
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    class PostViewHolder(view: View, val postinterface: postinterface) :
        RecyclerView.ViewHolder(view) {
        fun bind(item: post) {

            if (item.profilresim.isNullOrEmpty() || item.profilresim == "null") { //null text olarak geldiği için hem null hem empty olmuyor o yüzden "null" olarak gelirse diye kontrol
                if (item.cinsiyet == "erkek") {
                    itemView.profilresmi.setBackgroundResource(R.drawable.icons8)
                } else {
                    itemView.profilresmi.setBackgroundResource(R.drawable.ic_kadin_resim)
                }
            } else {
                imageclass.imgload(itemView.context, item.profilresim!!, itemView.profilresmi)
            }



            imageclass.imgload(itemView.context, item.postresim, itemView.postresmi)
            itemView.text_isim.text = item.name
            itemView.tarih.text = item.tarih
            itemView.aciklama.text = item.aciklama

            itemView.postresmi.setOnClickListener {
                postinterface.clickpost(item)
            }
            itemView.profilresmi.setOnClickListener {
                postinterface.clickprofile(item)
            }
            itemView.text_isim.setOnClickListener {
                postinterface.clickprofile(item)
            }
            itemView.kalp.setOnClickListener {
                postinterface.clicklike(item)
            }
            itemView.popup.setOnClickListener {
                var popupmenu = PopupMenu(itemView.context, it)
                popupmenu.inflate(R.menu.popup)
                popupmenu.setOnMenuItemClickListener {
                    MainActivity.openMainact(itemView.context)
                    true
                }
                popupmenu.show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return PostViewHolder(view, postinterface)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(list[position])

    }
}