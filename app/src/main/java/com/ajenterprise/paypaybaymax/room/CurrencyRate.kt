package com.ajenterprise.paypaybaymax.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_rate")
data class CurrencyRate(
    @PrimaryKey val position: Int,
    @ColumnInfo(name = "currency_name") val name: String?,
    @ColumnInfo(name = "currency_rate") val rate: Float?
)
