package com.ajenterprise.currencyconvertor.features.home.network

import com.ajenterprise.currencyconvertor.network.RetrofitApiClient
import retrofit2.Call
import retrofit2.http.GET

interface HomeApi{
    @GET("/live?${RetrofitApiClient.ACCESS_KEY}=${RetrofitApiClient.API_KEY}&format=1")
    fun getCurrency(): Call<CurrencyResponse>
    }