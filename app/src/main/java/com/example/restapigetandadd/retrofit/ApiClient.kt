package com.example.restapigetandadd.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    const val BASE_URl = "https://hvax.pythonanywhere.com/"

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): MyRetrofitService {
        return getRetrofit().create(MyRetrofitService::class.java)
    }
}