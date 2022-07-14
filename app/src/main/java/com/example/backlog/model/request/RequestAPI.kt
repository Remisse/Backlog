package com.example.backlog.model.request

import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface RequestAPI {
    @FormUrlEncoded
    @GET("/?search={search}")
    fun searchGame(
        @Field("search") search: String?,
        callback: Callback<Response<*>>?
    )
}
