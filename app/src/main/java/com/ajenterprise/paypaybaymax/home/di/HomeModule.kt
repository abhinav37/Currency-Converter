package com.ajenterprise.paypaybaymax.home.di

import com.ajenterprise.paypaybaymax.home.HomePresenter
import com.ajenterprise.paypaybaymax.home.network.HomeRepository
import com.ajenterprise.paypaybaymax.home.HomeViewProxy
import dagger.Binds
import dagger.Module

@Module
interface HomeModule {

    @Binds
    fun bindsHomeViewProxy(viewProxy: HomeViewProxy): HomeContract.ViewProxy

    @Binds
    fun bindsHomePresenter(presenter: HomePresenter): HomeContract.Presenter

//    @Binds
//    fun bindsHomeViewProxy(viewProxy: HomeViewProxy): HomeContract.ViewProxy

    @Binds
    fun bindsRepository(logger: HomeRepository): HomeContract.Repository
}
