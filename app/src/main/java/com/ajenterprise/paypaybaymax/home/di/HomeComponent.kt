package com.ajenterprise.paypaybaymax.home.di

import android.view.View
import com.ajenterprise.paypaybaymax.home.HomeActivity
import com.ajenterprise.paypaybaymax.home.network.HomeApi
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

    }

    fun inject(activity: HomeActivity)
}