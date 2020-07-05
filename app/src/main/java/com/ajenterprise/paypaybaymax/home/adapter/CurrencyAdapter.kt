package com.ajenterprise.paypaybaymax.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajenterprise.paypaybaymax.R
import com.ajenterprise.paypaybaymax.home.network.CurrencyResponse
import com.ajenterprise.paypaybaymax.utils.FlagRetriever
import kotlinx.android.synthetic.main.layout_item_currency_grid.view.flag
import kotlinx.android.synthetic.main.layout_item_currency_grid.view.rate
import javax.inject.Inject

class CurrencyAdapter @Inject constructor(val flagRetriever: FlagRetriever) :
    RecyclerView.Adapter<CurrencyViewHolder>() {
    private val currencyList: MutableList<CurrencyResponse.CurrencyData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            when (viewType) {
                ListViewType.GRID.viewType -> {
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_item_currency_list, parent)
                }
                else -> {
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_item_currency_grid, parent)
                }
            }
        )
    }

    override fun getItemCount(): Int = currencyList.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {

    }

    enum class ListViewType(val viewType: Int) {
        GRID(1),
        LIST(2)
    }


}

class CurrencyViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
    val countryFlag: ImageView = containerView.flag
    val rate: TextView = containerView.rate
}