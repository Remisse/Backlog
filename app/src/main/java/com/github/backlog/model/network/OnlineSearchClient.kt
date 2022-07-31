package com.github.backlog.model.network

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class OnlineSearchClient(url: String) {
    private val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

    val service = retrofit.create(OnlineSearchService::class.java)
}
