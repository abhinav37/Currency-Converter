package com.ajenterprise.paypaybaymax.home

import com.ajenterprise.paypaybaymax.home.di.HomeContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomePresenter @Inject constructor(
    private val viewProxy: HomeContract.ViewProxy,
    private val repository: HomeContract.Repository
) : HomeContract.Presenter, CoroutineScope by MainScope() {
    override fun onCreateActivity() {
//        viewProxy.setupAdapter()
//        viewProxy
        launch {
            repository.getCurrency(0)
        }
    }

    override fun onDestroy() {
        cancel()
    }
}