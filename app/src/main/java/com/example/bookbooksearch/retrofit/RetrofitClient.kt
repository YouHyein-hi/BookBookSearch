package com.example.bookbooksearch.retrofit

import com.example.bookbooksearch.NetworkInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val baseUrl = "https://openapi.naver.com/" // 오류나서 강제로 https로 바꿨는데 나중에 http로 다시 바꿔서 오류 해결해보자

    private var gson: Gson = GsonBuilder().setLenient().create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(NetworkInterceptor())
        .build()

    private fun getRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    fun getRetrofitService(): NaverAPI = getRetrofit().create(NaverAPI::class.java)
}