package com.ajenterprise.paypaybaymax.features.home.di

import android.os.Bundle
import com.ajenterprise.paypaybaymax.features.home.adapter.CurrencyAdapter
import com.ajenterprise.paypaybaymax.room.CurrencyRate

interface HomeContract {
    interface Presenter {
        fun onCreateActivity()
        fun onDestroy()
        fun onSaveInstanceState(outState: Bundle)
        fun onRestoreInstanceState(savedInstanceState: Bundle)
    }

    interface ViewProxy {
        fun setup(refreshAction: () -> Unit, onItemChange: (Int) -> Unit)
        fun updateList(list: List<CurrencyRate>)
        fun updateRateText(rate: Float)
        fun updateRecyclerLayout(viewType: CurrencyAdapter.ListViewType)
        fun showErrorToast()
        fun showLoading()
        fun hideLoading()
        fun updateConversionRate(conversionRate: Float)
        fun showErrorScreen()
        fun hideErrorScreen()
        fun selectPosition(selectedPosition: Int)
        fun getViewType(): CurrencyAdapter.ListViewType
        fun setViewType(viewType: CurrencyAdapter.ListViewType)
    }

    interface Repository {
        suspend fun getRemoteCurrencyList(): List<CurrencyRate>?
        suspend fun getLocalCurrencyList(): List<CurrencyRate>?
        suspend fun getLatestTimestampOnData(): Long
        suspend fun setLatestTimestampOnData(timestamp: Long): Boolean
        suspend fun setLatestCurrencyList(list: List<CurrencyRate>): Boolean
    }
}