package com.example.myrestapi.retrofit

import com.example.myrestapi.models.MyTodo
import com.example.myrestapi.models.TodoPostRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("plan")
    suspend fun getAllTodo():List<MyTodo>

    @POST("plan/")
    suspend fun addTodo(@Body todoPostRequest:TodoPostRequest):MyTodo
}