package com.ajenterprise.paypaybaymax.home.network

import com.ajenterprise.paypaybaymax.network.RetrofitApiClient
import retrofit2.Call
import retrofit2.http.GET

interface HomeApi{
    //TODO FIX ME
    @GET("/live?${RetrofitApiClient.ACCESS_KEY}=${RetrofitApiClient.API_KEY}&format=1")
    fun getCurrency(): Call<CurrencyResponse>
    }