package com.example.animeproje

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class imageclass2 {
    companion object imageclass {
        fun imgload2(context: Context, imageUrl: String, imageView: ImageView) {
            Glide.with(context).load(imageUrl).placeholder(R.drawable.ic_kadin_resim).into(imageView)
        }
    }
}