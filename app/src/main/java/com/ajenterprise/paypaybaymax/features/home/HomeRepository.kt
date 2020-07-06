package com.ajenterprise.paypaybaymax.features.home

import com.ajenterprise.paypaybaymax.features.home.di.HomeContract
import com.ajenterprise.paypaybaymax.features.home.network.HomeApi
import com.ajenterprise.paypaybaymax.room.CurrencyDao
import com.ajenterprise.paypaybaymax.room.CurrencyMeta
import com.ajenterprise.paypaybaymax.room.CurrencyRate
import retrofit2.await
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val api: HomeApi,
    private val currencyDao: CurrencyDao
) : HomeContract.Repository {

    override suspend fun getRemoteCurrencyList(): List<CurrencyRate>? {
        val response = api.getCurrency().await()
        var position = 0
        return response.quotes?.map {
            CurrencyRate(position++, it.key.removePrefix(PREFIX), it.value)
        }
    }

    override suspend fun getLocalCurrencyList(): List<CurrencyRate>? {
        return currencyDao.getAll()
    }

    override suspend fun getLatestTimestampOnData(): Long =
        currencyDao.getLatestTimeStamp()?.timestamp ?: 0


    override suspend fun setLatestTimestampOnData(timestamp: Long): Boolean {
        val currencyMeta = CurrencyMeta(id = TIMESTAMP_ID, timestamp = timestamp)
        currencyDao.insertMeta(currencyMeta)
        return true
    }

    override suspend fun setLatestCurrencyList(list: List<CurrencyRate>): Boolean {
        currencyDao.insertAllRates(list)
        return true
    }


    companion object {
        const val PREFIX = "USD"
        const val TIMESTAMP_ID = 0L
    }
}