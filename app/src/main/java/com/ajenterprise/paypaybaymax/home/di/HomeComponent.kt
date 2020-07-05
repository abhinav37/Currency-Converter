package com.ajenterprise.paypaybaymax.home.di

import com.ajenterprise.paypaybaymax.home.HomeActivity
import dagger.Component

@Component(
    modules = [HomeModule::class]
)
interface HomeComponent {
    @Component.Builder
    interface Builder {
        fun build(): HomeComponent
    }

    fun inject(activity: HomeActivity)
}