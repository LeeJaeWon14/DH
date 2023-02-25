package com.jeepchief.dh.model.rest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroClient {
    const val BASE_URL = "https://api.neople.co.kr/df/"

    private var instance: Retrofit? = null
    private val httpClient = OkHttpClient.Builder().apply {
        addNetworkInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
    }

    @Synchronized
    fun getInstance() : Retrofit {
        instance?.let {
            return it
        } ?: run {
            instance = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(httpClient.build())
            }.build()
            return instance!!
        }
    }
}