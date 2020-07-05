package com.ajenterprise.paypaybaymax.home

import androidx.annotation.VisibleForTesting
import com.ajenterprise.paypaybaymax.home.di.HomeContract
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val viewProxy: HomeContract.ViewProxy,
    private val repository: HomeContract.Repository
) : HomeContract.Presenter, CoroutineScope by MainScope() {
    private var latestResponse: List<Pair<String, Float>>? = null

    override fun onCreateActivity() {
        viewProxy.setup(this::fetchAndPopulate, this::onItemChange)
        viewProxy.showLoading()
        fetchAndPopulate()
    }

    @VisibleForTesting
    internal fun onItemChange(position: Int) {
        viewProxy.updateConversionRate(latestResponse?.getOrNull(position)?.second ?: 1f)
    }

    @VisibleForTesting
    internal fun fetchAndPopulate() {
        launch {
            val response = getCurrencyRates()
            viewProxy.hideLoading()
            if (response != null) {
                latestResponse = removePrefixFromList(response, PREFIX)
                viewProxy.updateList(latestResponse!!)
                onItemChange(0)
            } else {
                viewProxy.showErrorToast()
            }
        }
    }

    private suspend fun getCurrencyRates(): Map<String, Float>? =
        try {
            val response = repository.getCurrency()
            response?.quotes
        } catch (ex: Exception) {
            if (ex is CancellationException) {
                throw ex
            }
            null
        }

    private fun removePrefixFromList(
        list: Map<String, Float>,
        prefix: String
    ): List<Pair<String, Float>> =
        list.mapKeys {
            it.key.removePrefix(prefix)
        }.toList()

    override fun onDestroy() {
        cancel()
    }

    companion object {
        const val PREFIX = "USD"
    }
}