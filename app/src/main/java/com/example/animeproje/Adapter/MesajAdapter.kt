package com.example.animeproje.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.animeproje.Fragment.mesajinterface
import com.example.animeproje.R
import com.example.animeproje.imageclass
import com.example.animeproje.model.mesaj
import com.example.animeproje.model.post
import kotlinx.android.synthetic.main.item.view.*
import kotlinx.android.synthetic.main.mesajitem.view.*

class MesajAdapter(
    val list: List<mesaj>, val mesajinterface: mesajinterface
) : RecyclerView.Adapter<MesajAdapter.MesajViewHolder>() {
    class MesajViewHolder(view: View, val mesajinterface: mesajinterface) :
        RecyclerView.ViewHolder(view) {
        fun bind(item: mesaj) {
            imageclass.imgload(itemView.context, item.profilresim, itemView.mesajlar_profile_resim)
            itemView.mesaj.text = item.mesaj
            itemView.isim.text = item.name
            itemView.mesaj_tarih.text = item.tarih
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: MesajViewHolder, position: Int) {
        holder.bind(list[position])

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MesajViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mesajitem, parent, false)
        return MesajViewHolder(view, mesajinterface)
    }

}