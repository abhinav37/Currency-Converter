package com.ajenterprise.paypaybaymax.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ajenterprise.paypaybaymax.R
import com.ajenterprise.paypaybaymax.home.di.DaggerHomeComponent
import javax.inject.Inject

class HomeActivity : AppCompatActivity() {
    @Inject
    lateinit var presenter: HomePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        DaggerHomeComponent.builder()
            .build()
            .inject(this)
        presenter.onCreateActivity()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }
}