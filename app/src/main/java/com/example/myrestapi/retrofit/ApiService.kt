package com.example.myrestapi.retrofit

import com.example.myrestapi.models.MyTodo
import com.example.myrestapi.models.MyTodoPostRequest
import com.example.myrestapi.models.TodoPostRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("plan")
    suspend fun getAllTodo():List<MyTodo>

    @POST("plan/")
    suspend fun addTodo(@Body todoPostRequest:TodoPostRequest):MyTodo

    @PATCH("plan/{id}/")
    suspend fun updateTodo(@Path ("id") id:Int, @Body myTodoPostRequest: MyTodoPostRequest):MyTodo

    @DELETE("plan/{id}/")
    suspend fun deleteTodo(@Path ("id") id:Int)
}