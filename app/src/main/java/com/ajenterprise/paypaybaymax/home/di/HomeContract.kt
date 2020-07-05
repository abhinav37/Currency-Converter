package com.ajenterprise.paypaybaymax.home.di

import com.ajenterprise.paypaybaymax.home.adapter.CurrencyAdapter
import com.ajenterprise.paypaybaymax.home.network.CurrencyResponse

interface HomeContract {
    interface Presenter {
        fun onCreateActivity()
        fun onDestroy()
    }

    interface ViewProxy {
        fun setup(refreshAction: () -> Unit, onItemChange: (Int) -> Unit)
        fun updateList(list: List<Pair<String, Float>>)
        fun updateRateText(rate: Float)
        fun updateRecyclerLayout(viewType: CurrencyAdapter.ListViewType)
        fun showErrorToast()
        fun showLoading()
        fun hideLoading()
        fun updateConversionRate(conversionRate: Float)
    }

    interface Repository {
        suspend fun getCurrency(): CurrencyResponse?
    }
}