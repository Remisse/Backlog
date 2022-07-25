package com.example.backlog.model.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIClient(url: String) {
    private val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

    val service = retrofit.create(OnlineSearchService::class.java)
}
