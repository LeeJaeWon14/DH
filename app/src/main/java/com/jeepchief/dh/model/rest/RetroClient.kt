package com.jeepchief.dh.model.rest

import com.jeepchief.dh.model.NetworkConstants
import com.jeepchief.dh.util.DHLog
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetroClient {
    const val BASE_URL = "https://api.neople.co.kr/df/"

    private var instance: Retrofit? = null
    private val httpClient = OkHttpClient.Builder().apply {
        addNetworkInterceptor(getLoggingInterceptor(HttpLoggingInterceptor.Level.BODY))
        addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(
                "apiKey",
                    NetworkConstants.API_KEY
                )
                .build()
            chain.proceed(request)
        }
        connectTimeout(60L, TimeUnit.SECONDS)
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

    private fun getLoggingInterceptor(level: HttpLoggingInterceptor.Level) : HttpLoggingInterceptor {
        val logger = HttpLoggingInterceptor.Logger { message -> DHLog.d(message) }
        return HttpLoggingInterceptor(logger).setLevel(level)
    }
}