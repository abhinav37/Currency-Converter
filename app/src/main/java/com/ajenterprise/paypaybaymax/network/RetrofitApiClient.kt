package com.ajenterprise.paypaybaymax.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiClient constructor(
    okHttpClient: OkHttpClient
) {
    private val retrofit: Retrofit

    init {
        retrofit = buildRetrofit(BASE_URL, okHttpClient)
    }

    fun <T> getApiInstance(apiServiceClass: Class<T>): T = retrofit.create(apiServiceClass)

    private fun buildRetrofit(baseUrl: String, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    companion object {
        const val API_KEY = "f207d8555e9bd0fa4a19ddc3e57e2d57"
        const val BASE_URL = "http://api.currencylayer.com/"
        const val ACCESS_KEY = "access_key"

    }
}
