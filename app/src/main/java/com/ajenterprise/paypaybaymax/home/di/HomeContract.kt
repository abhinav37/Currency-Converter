package com.ajenterprise.paypaybaymax.home.di

import com.ajenterprise.paypaybaymax.home.network.CurrencyResponse

interface HomeContract {
    interface Presenter{
        fun onCreateActivity()
        fun onDestroy()
    }
    interface ViewProxy{}
    interface Repository{
        suspend fun getCurrency(id: Int): CurrencyResponse?
    }
}