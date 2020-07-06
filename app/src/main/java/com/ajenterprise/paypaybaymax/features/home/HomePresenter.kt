package com.ajenterprise.paypaybaymax.features.home

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import com.ajenterprise.paypaybaymax.features.home.adapter.CurrencyAdapter
import com.ajenterprise.paypaybaymax.features.home.di.HomeContract
import com.ajenterprise.paypaybaymax.room.CurrencyRate
import com.ajenterprise.paypaybaymax.utils.DispatchersFactory
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val viewProxy: HomeContract.ViewProxy,
    private val repository: HomeContract.Repository,
    private val dispatchers: DispatchersFactory
) : HomeContract.Presenter, CoroutineScope by MainScope() {
    @VisibleForTesting
    internal var list: List<CurrencyRate>? = null

    @VisibleForTesting
    internal var selectedPosition: Int = 0

    override fun onCreateActivity() {
        viewProxy.setup(this::fetchAndPopulate, this::onItemChange)
        fetchAndPopulate()
    }

    @VisibleForTesting
    internal fun onItemChange(position: Int) {
        viewProxy.updateConversionRate(list?.getOrNull(position)?.rate ?: 1f)
        selectedPosition = position
    }

    @VisibleForTesting
    internal fun fetchAndPopulate() {
        launch {
            viewProxy.hideErrorScreen()
            viewProxy.showLoading()
            val timestamp = System.currentTimeMillis()
            val latestDataTimestamp = getLatestTimeStamp()
            val list = if (timestamp > latestDataTimestamp + 1800000) {
                // Get Local data if network fails
                getRemoteCurrencyRates()?.apply {
                    setCurrentRates(this)
                    setLatestTimeStamp(timestamp)
                } ?: getLocalCurrencyRates()
            } else {
                getLocalCurrencyRates()
            }

            viewProxy.hideLoading()

            if (!list.isNullOrEmpty()) {
                this@HomePresenter.list = list
                viewProxy.updateList(list)
                viewProxy.selectPosition(selectedPosition)
                onItemChange(selectedPosition)
            } else {
                viewProxy.showErrorToast()
                viewProxy.showErrorScreen()
            }
        }
    }

    private suspend fun getLocalCurrencyRates(): List<CurrencyRate>? = withContext(dispatchers.io) {
        try {
            repository.getLocalCurrencyList()
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            null
        }
    }

    private suspend fun getRemoteCurrencyRates(): List<CurrencyRate>? =
        withContext(dispatchers.io) {
            try {
                repository.getRemoteCurrencyList()
            } catch (ex: Exception) {
                if (ex is CancellationException) {
                    throw ex
                }
                null
            }
        }

    private suspend fun getLatestTimeStamp(): Long = withContext(dispatchers.io) {
        try {
            repository.getLatestTimestampOnData()
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            0L
        }
    }

    private suspend fun setLatestTimeStamp(timestamp: Long): Boolean = withContext(dispatchers.io) {
        try {
            repository.setLatestTimestampOnData(timestamp)
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            false
        }
    }

    private suspend fun setCurrentRates(list: List<CurrencyRate>): Boolean =
        withContext(dispatchers.io) {
            try {
                repository.setLatestCurrencyList(list)
            } catch (ex: Exception) {
                if (ex is CancellationException) {
                    throw ex
                }
                false
            }
        }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_POSITION, selectedPosition)
        outState.putSerializable(VIEW_TYPE, viewProxy.getViewType())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        selectedPosition = savedInstanceState.getInt(SELECTED_POSITION, 0)
        viewProxy.setViewType(savedInstanceState.getSerializable(VIEW_TYPE) as CurrencyAdapter.ListViewType)
    }

    override fun onDestroy() {
        cancel()
    }

    companion object {
        const val SELECTED_POSITION = "selected_position"
        const val VIEW_TYPE = "view_type"
    }
}