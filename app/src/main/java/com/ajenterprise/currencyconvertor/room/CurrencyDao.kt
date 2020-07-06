package com.ajenterprise.currencyconvertor.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_rate")
    fun getAll(): List<CurrencyRate>?

    @Query("SELECT * FROM currency_meta WHERE id is 0")
    fun getLatestTimeStamp(): CurrencyMeta?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllRates(currencies: List<CurrencyRate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMeta(vararg currencies: CurrencyMeta)

    @Query("DELETE FROM currency_rate")
    fun deleteAllCurrencyRate()

    @Query("DELETE FROM currency_meta")
    fun deleteAllCurrencyMeta()
}