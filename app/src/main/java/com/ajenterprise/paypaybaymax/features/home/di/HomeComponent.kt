package com.ajenterprise.paypaybaymax.features.home.di

import android.view.View
import com.ajenterprise.paypaybaymax.features.home.HomeActivity
import com.ajenterprise.paypaybaymax.features.home.network.HomeApi
import com.ajenterprise.paypaybaymax.room.CurrencyDao
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [HomeModule::class]
)
interface HomeComponent {
    @Component.Builder
    interface Builder {
        fun build(): HomeComponent

        @BindsInstance
        fun homeApi(homeApi: HomeApi): Builder

        @BindsInstance
        fun view(view: View): Builder

        @BindsInstance
        fun databaseDao(currencyDao: CurrencyDao): Builder
    }

    fun inject(activity: HomeActivity)
}