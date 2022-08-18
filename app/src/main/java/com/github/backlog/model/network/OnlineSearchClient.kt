package com.github.backlog.model.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val URL = "https://www.google.com"

class OnlineSearchClient() {
    private val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

    val service: OnlineSearchService = retrofit.create(OnlineSearchService::class.java)
}
