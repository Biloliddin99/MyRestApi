package com.example.myrestapi.repository

import com.example.myrestapi.models.MyTodo
import com.example.myrestapi.models.MyTodoPostRequest
import com.example.myrestapi.models.TodoPostRequest
import com.example.myrestapi.retrofit.ApiService

class TodoRepository(private val apiService: ApiService) {
    suspend fun getAllTodo() = apiService.getAllTodo()
    suspend fun addTodo(todoPostRequest: TodoPostRequest) = apiService.addTodo(todoPostRequest)
    suspend fun updateTodo(id:Int,myTodoPostRequest: MyTodoPostRequest) = apiService.updateTodo(id,myTodoPostRequest)
    suspend fun deleteTodo(id: Int) = apiService.deleteTodo(id)
}