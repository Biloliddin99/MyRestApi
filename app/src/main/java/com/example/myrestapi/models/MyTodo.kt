package com.example.myrestapi.models

data class MyTodo(
    val bajarildi: Boolean,
    val batafsil: String,
    val id: Int,
    var oxirgi_muddat: String,
    val sana: String,
    val sarlavha: String,
    val zarurlik: String
)
