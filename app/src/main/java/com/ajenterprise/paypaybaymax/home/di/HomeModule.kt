package com.ajenterprise.paypaybaymax.home.di

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import com.ajenterprise.paypaybaymax.R
import com.ajenterprise.paypaybaymax.home.HomePresenter
import com.ajenterprise.paypaybaymax.home.HomeViewProxy
import com.ajenterprise.paypaybaymax.home.network.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [HomeModule.ToastModule::class])
interface HomeModule {

    @Binds
    fun bindsHomeViewProxy(viewProxy: HomeViewProxy): HomeContract.ViewProxy

    @Binds
    fun bindsHomePresenter(presenter: HomePresenter): HomeContract.Presenter

    @Binds
    fun bindsRepository(logger: HomeRepository): HomeContract.Repository

    @Module
    class ToastModule {
        @Provides
        fun provideToast(view: View): Toast =
            Toast.makeText(view.context, R.string.error_toast, Toast.LENGTH_LONG)

        @Provides
        fun provideArrayAdapter(view: View): ArrayAdapter<String> = ArrayAdapter<String>(
            view.context, android.R.layout.simple_spinner_item, ArrayList()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
    }
}
