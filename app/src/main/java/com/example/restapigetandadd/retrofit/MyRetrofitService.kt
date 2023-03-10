package com.example.restapigetandadd.retrofit

import com.example.restapigetandadd.models.User
import com.example.restapigetandadd.models.myTodoRequest
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface MyRetrofitService {

    @GET("plan")
    fun getAllTodo():Call<List<User>>

    @POST("plan/")
    fun addTodo(@Body myTodoRequest: myTodoRequest):Call<User>

    @DELETE("plan/{id}/")
    fun deleteToDo(@Path("id") id:Int):Call<Int>

    @PUT("plan/{id}/")
    fun updateToDo(@Path("id") id: Int,@Body myTodoRequest: myTodoRequest):Call<User>
}