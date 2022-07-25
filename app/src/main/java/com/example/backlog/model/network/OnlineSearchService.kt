package com.example.backlog.model.network

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET

interface OnlineSearchService {
    @FormUrlEncoded
    @GET("/?search={search}")
    suspend fun searchGame(@Field("search") search: String?)
}
