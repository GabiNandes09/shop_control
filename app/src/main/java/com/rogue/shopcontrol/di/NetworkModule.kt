package com.rogue.shopcontrol.di

import com.rogue.shopcontrol.data.remote.api.NfceApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        level =
                            HttpLoggingInterceptor.Level.BODY
                    }
            )
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl("https://placeholder.com/")
            .client(get())
            .addConverterFactory(
                Json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
    }
    single {

        Retrofit.Builder()
            .baseUrl(
                "https://www.nfce.fazenda.sp.gov.br/"
            )
            .client(get())
            .addConverterFactory(
                ScalarsConverterFactory.create()
            )
            .build()

    }

    single {

        get<Retrofit>()
            .create(NfceApi::class.java)

    }
}