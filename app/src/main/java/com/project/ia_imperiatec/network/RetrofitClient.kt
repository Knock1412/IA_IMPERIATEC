package com.project.ia_imperiatec.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    val instance: IAService by lazy {
        Retrofit.Builder()
            .baseUrl("http://192.168.10.109:5000/") // ← Remplace si IP change
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IAService::class.java)
    }
}
