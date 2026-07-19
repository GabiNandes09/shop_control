package com.rogue.shopcontrol.data.remote.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface NfceApi {
    @GET
    suspend fun getNfceHtml(
        @Url url: String
    ): Response<String>

}