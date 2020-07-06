package com.ajenterprise.paypaybaymax.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency_meta")
data class CurrencyMeta(
    @PrimaryKey(autoGenerate = true) val id: Long? = 1,
    @ColumnInfo(name = "timestamp") val timestamp: Long?
)
