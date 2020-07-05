package com.ajenterprise.paypaybaymax.home.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface HomeApi{
        //TODO FIX ME
        @GET("/todos/{id}")
        fun getCurrency(@Path(value = "id") todoId: Int): Call<CurrencyResponse>
    }