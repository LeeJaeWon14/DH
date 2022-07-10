package com.jeepchief.dh.model.rest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {
    const val BASE_URL = "https://api.neople.co.kr/df/"

    private var instance: Retrofit? = null

    @Synchronized
    fun getInstance() : Retrofit {
        instance?.let {
            return it
        } ?: run {
            instance = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
            }.build()
            return instance!!
        }
    }
}