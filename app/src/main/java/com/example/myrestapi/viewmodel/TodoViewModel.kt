package com.example.myrestapi.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrestapi.models.MyTodo
import com.example.myrestapi.models.MyTodoPostRequest
import com.example.myrestapi.models.TodoPostRequest
import com.example.myrestapi.repository.TodoRepository
import com.example.myrestapi.retrofit.ApiClient
import com.example.myrestapi.utils.Resources
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TodoViewModel(private val todoRepository: TodoRepository) : ViewModel() {
    private val liveData = MutableLiveData<Resources<List<MyTodo>>>()

    fun getAllTodo(): MutableLiveData<Resources<List<MyTodo>>> {


        viewModelScope.launch {
            liveData.postValue(Resources.loading("loading"))
            try {
                coroutineScope {
                    val list = async {
                        todoRepository.getAllTodo()
                    }.await()
                    liveData.postValue(Resources.success(list))
                }

            } catch (e: Exception) {
                liveData.postValue(Resources.error(e.message))
            }
        }
        return liveData
    }

    private val postLiveData = MutableLiveData<Resources<MyTodo>>()
    fun addTodo(todoPostRequest: TodoPostRequest): MutableLiveData<Resources<MyTodo>> {
        viewModelScope.launch {
            postLiveData.postValue(Resources.loading("Loading"))
            try {
                coroutineScope {
                    val response = async {
                        todoRepository.addTodo(todoPostRequest)
                    }.await()
                    postLiveData.postValue(Resources.success(response))
                    getAllTodo()
                }
            } catch (e: Exception) {
                postLiveData.postValue(Resources.error(e.message))
            }
        }
        return postLiveData
    }

    private val liveDataUpdate = MutableLiveData<Resources<MyTodo>>()
    fun updateMyTodo(
        id: Int, myTodoPostRequest: MyTodoPostRequest): MutableLiveData<Resources<MyTodo>> {

        viewModelScope.launch {
            liveDataUpdate.postValue(Resources.loading("Loading update"))
            try {
                coroutineScope {
                    val response = async {
                        todoRepository.updateTodo(id, myTodoPostRequest)
                    }.await()
                    liveDataUpdate.postValue(Resources.success(response))
                    getAllTodo()
                }
            } catch (e: Exception) {
                liveDataUpdate.postValue(Resources.error(e.message))
            }
        }
        return liveDataUpdate

    }

    fun deleteTodo(id: Int){

        viewModelScope.launch {
            try {
                coroutineScope {
                    launch {
                        todoRepository.deleteTodo(id)
                    }

                    getAllTodo()
                }
            } catch (e: Exception) {

            }
        }

    }
}