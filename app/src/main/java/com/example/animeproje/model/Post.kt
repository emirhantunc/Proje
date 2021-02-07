package com.example.animeproje.model

data class post(
    var name: String,
    var tarih: String,
    var postresim: String,
    var profilresim: String?,
    var aciklama: String,
    var cinsiyet:String,
    var userId:String
)
data class mesaj(
    var name:String,
    var tarih:String,
    var profilresim: String,
    var mesaj:String
)