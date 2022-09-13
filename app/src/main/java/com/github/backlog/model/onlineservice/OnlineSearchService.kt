package com.github.backlog.model.onlineservice

import com.github.backlog.model.onlineservice.adapter.GameDetailed
import com.github.backlog.model.onlineservice.adapter.GameJson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface OnlineSearchService {

    @GET("/steamlibrary/{steamId}")
    suspend fun steamLibrary(@Path("steamId") steamId: String?): List<GameJson>

    @GET("/rawgsearch/{search}")
    suspend fun searchOnRawg(@Path("search") search: String?): List<GameJson>

    @GET("/steamdetails/{appid}")
    suspend fun steamDetails(@Path("appId") appId: String?): GameDetailed

    @GET("/rawgdetails/{appId}")
    suspend fun rawgDetails(@Path("appId") appId: String?): GameDetailed
}
