package com.ajenterprise.paypaybaymax.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ajenterprise.paypaybaymax.R
import com.ajenterprise.paypaybaymax.home.di.DaggerHomeComponent
import com.ajenterprise.paypaybaymax.home.network.HomeApi
import com.ajenterprise.paypaybaymax.network.RetrofitApiClient
import okhttp3.OkHttpClient
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        DaggerHomeComponent.builder()
            .homeApi(RetrofitApiClient(OkHttpClient.Builder().build()).getApiInstance(HomeApi::class.java))
            .view(findViewById<View>(android.R.id.content).rootView)
            .build()
            .inject(this)
        presenter.onCreateActivity()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}