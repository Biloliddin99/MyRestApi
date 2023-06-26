package com.example.myrestapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myrestapi.repository.TodoRepository

class MyViewModelFactory(val todoRepository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            return TodoViewModel(todoRepository) as T
        }
        throw IllegalArgumentException("Error")
    }
}