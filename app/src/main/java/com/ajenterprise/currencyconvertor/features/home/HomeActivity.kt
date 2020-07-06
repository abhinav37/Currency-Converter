package com.ajenterprise.currencyconvertor.features.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ajenterprise.currencyconvertor.R
import com.ajenterprise.currencyconvertor.features.home.di.DaggerHomeComponent
import com.ajenterprise.currencyconvertor.features.home.di.HomeContract
import com.ajenterprise.currencyconvertor.features.home.network.HomeApi
import com.ajenterprise.currencyconvertor.network.RetrofitApiClient
import com.ajenterprise.currencyconvertor.room.AppDatabase
import okhttp3.OkHttpClient
import javax.inject.Inject


class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var presenter: HomeContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(findViewById(R.id.toolbar))
        DaggerHomeComponent.builder()
            .homeApi(
                RetrofitApiClient(
                    OkHttpClient.Builder().build()
                )
                    .getApiInstance(HomeApi::class.java)
            )
            .view(findViewById<View>(android.R.id.content).rootView)
            .databaseDao(AppDatabase.getDatabase(application).currencyDao())
            .build()
            .inject(this)
        if (savedInstanceState != null) {
            presenter.onRestoreInstanceState(savedInstanceState)
        }
        presenter.onCreateActivity()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        presenter.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}