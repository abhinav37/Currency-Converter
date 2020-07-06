package com.ajenterprise.currencyconvertor.features.home.di

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajenterprise.currencyconvertor.R
import com.ajenterprise.currencyconvertor.features.home.HomePresenter
import com.ajenterprise.currencyconvertor.features.home.HomeRepository
import com.ajenterprise.currencyconvertor.features.home.HomeViewProxy
import com.ajenterprise.currencyconvertor.utils.DefaultDispatchersFactory
import com.ajenterprise.currencyconvertor.utils.DispatchersFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import java.text.DecimalFormat

@Module(includes = [HomeModule.ToastModule::class])
interface HomeModule {

    @Binds
    fun bindsHomeViewProxy(viewProxy: HomeViewProxy): HomeContract.ViewProxy

    @Binds
    fun bindsHomePresenter(presenter: HomePresenter): HomeContract.Presenter

    @Binds
    fun bindsRepository(logger: HomeRepository): HomeContract.Repository

    @Binds
    fun bindsDispatcherFactory(dispatchersFactory: DefaultDispatchersFactory): DispatchersFactory

    @Module
    class ToastModule {
        @Provides
        fun provideToast(view: View): Toast =
            Toast.makeText(view.context, R.string.error_toast, Toast.LENGTH_LONG)

        @Provides
        fun provideArrayAdapter(view: View): ArrayAdapter<String> = ArrayAdapter<String>(
            view.context, R.layout.layout_spinner_item, ArrayList()
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        @Provides
        fun provideGridLayoutManager(view: View): GridLayoutManager = GridLayoutManager(
            view.context,
            view.resources.getInteger(R.integer.grid_col_count)
        )

        @Provides
        fun provideListLayoutManager(view: View): LinearLayoutManager =
            LinearLayoutManager(view.context)

        @Provides
        fun provideDecimalFormatter(): DecimalFormat = DecimalFormat(PATTERN)

        companion object {
            private const val PATTERN = "##,###,###,###"
        }
    }

}
