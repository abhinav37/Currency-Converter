package com.ajenterprise.paypaybaymax.home.network

import com.ajenterprise.paypaybaymax.home.di.HomeContract
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import javax.inject.Inject

class HomeRepository @Inject constructor(private val api: HomeApi) : HomeContract.Repository {

  override suspend fun getCurrency(): CurrencyResponse? {
        return api.getCurrency().await()
    }
}