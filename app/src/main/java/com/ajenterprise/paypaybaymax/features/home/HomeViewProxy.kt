package com.ajenterprise.paypaybaymax.features.home

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajenterprise.paypaybaymax.R
import com.ajenterprise.paypaybaymax.features.home.adapter.CurrencyAdapter
import com.ajenterprise.paypaybaymax.features.home.adapter.CurrencyAdapter.ListViewType.GRID
import com.ajenterprise.paypaybaymax.features.home.di.HomeContract
import com.ajenterprise.paypaybaymax.room.CurrencyRate
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_home.currency_spinner
import kotlinx.android.synthetic.main.activity_home.error_screen
import kotlinx.android.synthetic.main.activity_home.grid_list_button
import kotlinx.android.synthetic.main.activity_home.progress_bar
import kotlinx.android.synthetic.main.activity_home.recycler_view
import kotlinx.android.synthetic.main.activity_home.refresh_button
import kotlinx.android.synthetic.main.activity_home.user_rate
import javax.inject.Inject


class HomeViewProxy @Inject constructor(
    private val currencySpinnerAdapter: ArrayAdapter<String>,
    private val currencyAdapter: CurrencyAdapter,
    private val gridLayoutManager: GridLayoutManager,
    private val listLayoutManager: LinearLayoutManager,
    private val listenerFactory: ListenerFactory,
    private val toast: Toast,
    override val containerView: View
) : HomeContract.ViewProxy, LayoutContainer {
    @VisibleForTesting
    internal var viewType: CurrencyAdapter.ListViewType = GRID

    override fun setup(refreshAction: () -> Unit, onItemChange: (Int) -> Unit) {
        recycler_view.apply {
            layoutManager = if (viewType == GRID) {
                gridLayoutManager
            } else {
                listLayoutManager
            }
            currencyAdapter.setListType(viewType)
            adapter = currencyAdapter
        }
        currency_spinner.adapter = currencySpinnerAdapter
        currency_spinner.onItemSelectedListener =
            listenerFactory.getOnItemSelectedListener(onItemChange)
        user_rate.doOnTextChanged(listenerFactory.getOnTextChanged(this::updateRateText))

        grid_list_button.setOnClickListener {
            if (viewType == GRID)
                updateRecyclerLayout(CurrencyAdapter.ListViewType.LIST)
            else
                updateRecyclerLayout(GRID)
        }
        refresh_button.setOnClickListener {
            refreshAction()
        }
    }

    override fun updateList(list: List<CurrencyRate>) {
        currencyAdapter.setAdapterAndRefresh(list)
        currencySpinnerAdapter.run {
            clear()
            addAll(list.map { it.name })
            notifyDataSetChanged()
        }
    }


    override fun updateRateText(rate: Float) {
        currencyAdapter.setCurrentRate(rate)
    }

    override fun updateConversionRate(conversionRate: Float) {
        currencyAdapter.setConversionRate(conversionRate)
    }

    override fun updateRecyclerLayout(viewType: CurrencyAdapter.ListViewType) {
        if (viewType == GRID) {
            recycler_view.layoutManager = gridLayoutManager
            grid_list_button.run {
                setImageDrawable(
                    resources.getDrawable(
                        R.drawable.list_icon,
                        null
                    )
                )
            }
        } else {
            recycler_view.layoutManager = listLayoutManager
            grid_list_button.run {
                setImageDrawable(
                    resources.getDrawable(
                        R.drawable.grid_icon,
                        null
                    )
                )
            }

        }
        this.viewType = viewType
        currencyAdapter.setListType(viewType)
    }

    override fun selectPosition(selectedPosition: Int) {
        currency_spinner.setSelection(selectedPosition)
    }

    override fun getViewType(): CurrencyAdapter.ListViewType = viewType
    override fun setViewType(viewType: CurrencyAdapter.ListViewType) {
        this.viewType = viewType
    }

    override fun showLoading() {
        progress_bar.isVisible = true
    }

    override fun hideLoading() {
        progress_bar.isVisible = false
    }

    override fun showErrorToast() {
        toast.show()
    }

    override fun showErrorScreen() {
        error_screen.isVisible = true
        recycler_view.isVisible = false
    }

    override fun hideErrorScreen() {
        error_screen.isVisible = false
        recycler_view.isVisible = true
    }
}