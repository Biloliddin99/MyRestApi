package com.example.myrestapi.models

data class TodoPostRequest(
    val sarlavha: String,
    val batafsil: String,
    val oxirgi_muddat: String,
    val zarurlik: String,
    val bajarildi: Boolean
)