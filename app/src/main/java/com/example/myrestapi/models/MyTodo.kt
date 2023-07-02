package com.example.myrestapi.models

data class MyTodo(
    var bajarildi: Boolean,
    var batafsil: String,
    val id: Int,
    var oxirgi_muddat: String,
    var sana: String,
    var sarlavha: String,
    var zarurlik: String
)
