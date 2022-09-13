package com.github.backlog.model.onlineservice

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val URL = "https://backlog-relay-dh8gkwxz7-remisse.vercel.app/"

class OnlineSearchClient() {
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(MoshiConverterFactory.create(moshi))
                    .build()

    val service: OnlineSearchService = retrofit.create(OnlineSearchService::class.java)
}
