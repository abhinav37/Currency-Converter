package com.ajenterprise.paypaybaymax.home

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ajenterprise.paypaybaymax.R
import com.ajenterprise.paypaybaymax.home.adapter.CurrencyAdapter
import com.ajenterprise.paypaybaymax.home.di.HomeContract
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_home.currency_spinner
import kotlinx.android.synthetic.main.activity_home.grid_list_button
import kotlinx.android.synthetic.main.activity_home.recycler_view
import kotlinx.android.synthetic.main.activity_home.swipe_refresh
import kotlinx.android.synthetic.main.activity_home.user_rate
import javax.inject.Inject


class HomeViewProxy @Inject constructor(
    private val currencySpinnerAdapter: ArrayAdapter<String>,
    private val currencyAdapter: CurrencyAdapter,
    private val toast: Toast,
    override val containerView: View
) : HomeContract.ViewProxy, LayoutContainer {
    private val gridLayoutManager =
        GridLayoutManager(
            containerView.context,
            containerView.resources.getInteger(R.integer.grid_col_count)
        )
    private val listLayoutManager = LinearLayoutManager(containerView.context)

    override fun setup(refreshAction: () -> Unit, onItemChange: (Int) -> Unit) {
        recycler_view.apply {
            layoutManager = gridLayoutManager
            adapter = currencyAdapter
        }
        currency_spinner.adapter = currencySpinnerAdapter
        currency_spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                onItemChange(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }
        swipe_refresh.setOnRefreshListener {
            refreshAction()
        }
        swipe_refresh.isRefreshing = true
        user_rate.doOnTextChanged { text, _, _, _ ->
            val rate = try {
                text.toString().toFloat()
            } catch (ex: Exception) {
                0f
            }
            updateRateText(rate)
        }
        grid_list_button.setOnClickListener {
            if (currencyAdapter.getViewType() == CurrencyAdapter.ListViewType.GRID)
                updateRecyclerLayout(CurrencyAdapter.ListViewType.LIST)
            else
                updateRecyclerLayout(CurrencyAdapter.ListViewType.GRID)
        }
    }

    override fun updateList(list: List<Pair<String, Float>>) {
        currencyAdapter.setAdapterAndRefresh(list)
        currencySpinnerAdapter.run {
            clear()
            addAll(list.map { it.first })
            notifyDataSetChanged()
        }
    }

    override fun updateRateText(rate: Float) {
        currencyAdapter.setCurrentRate(rate)
    }

    override fun updateConversionRate(conversionRate: Float) {
        currencyAdapter.serConversionRate(conversionRate)
    }

    override fun updateRecyclerLayout(viewType: CurrencyAdapter.ListViewType) {
        if (viewType == CurrencyAdapter.ListViewType.GRID) {
            recycler_view.layoutManager = gridLayoutManager

        } else {
            recycler_view.layoutManager = listLayoutManager
        }
        currencyAdapter.setListType(viewType)
    }

    override fun showLoading() {
        swipe_refresh.isRefreshing = true
    }

    override fun hideLoading() {
        swipe_refresh.isRefreshing = false
    }

    override fun showErrorToast() {
        toast.show()
    }
}