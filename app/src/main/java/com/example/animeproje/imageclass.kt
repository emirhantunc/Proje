package com.example.animeproje

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class imageclass {
    companion object imageclass {
        fun imgload(context: Context, imageUrl: String, imageView: ImageView) {
            Glide.with(context).load(imageUrl).into(imageView)
        }
    }
}