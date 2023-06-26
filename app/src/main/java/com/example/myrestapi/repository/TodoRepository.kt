package com.example.myrestapi.repository

import com.example.myrestapi.models.TodoPostRequest
import com.example.myrestapi.retrofit.ApiService

class TodoRepository(val apiService: ApiService) {
    suspend fun getAllTodo() = apiService.getAllTodo()
    suspend fun addTodo(todoPostRequest: TodoPostRequest) = apiService.addTodo(todoPostRequest)
}