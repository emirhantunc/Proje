package com.example.animeproje

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide

class imageclass {
    companion object imageclass {
        fun imgload(a: Context, b: String, c: ImageView) {
            Glide.with(a).load(b).into(c)
        }
    }
}