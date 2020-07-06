package com.ajenterprise.currencyconvertor.features.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ajenterprise.currencyconvertor.R
import com.ajenterprise.currencyconvertor.room.CurrencyRate
import com.ajenterprise.currencyconvertor.utils.FlagRetriever
import kotlinx.android.synthetic.main.layout_item_currency_grid.view.flag
import kotlinx.android.synthetic.main.layout_item_currency_grid.view.rate
import kotlinx.android.synthetic.main.layout_item_currency_list.view.currency
import java.text.DecimalFormat
import javax.inject.Inject

class CurrencyAdapter @Inject constructor(private val flagRetriever: FlagRetriever) :
    RecyclerView.Adapter<CurrencyViewHolder>() {
    private var currencyList: List<CurrencyRate> = emptyList()
    private var viewType: ListViewType =
        ListViewType.GRID
    private var currentRate: Float =
        BASE_CURRENT_RATE
    private var conversionRate: Float =
        BASE_CONVERSION_RATE
    private var formatter = DecimalFormat(PATTERN)

    fun setCurrentRate(currentRate: Float) {
        this.currentRate = currentRate
        notifyItemRangeChanged(0, currencyList.size)
    }

    fun setConversionRate(conversionRate: Float) {
        this.conversionRate = conversionRate
        notifyItemRangeChanged(0, currencyList.size)
    }

    fun setListType(viewType: ListViewType) {
        this.viewType = viewType
        notifyDataSetChanged()
    }

    fun setAdapterAndRefresh(currencyList: List<CurrencyRate>) {
        this.currencyList = currencyList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder(
            when (viewType) {
                ListViewType.GRID.viewType -> {
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_item_currency_grid, parent, false)
                }
                else -> {
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.layout_item_currency_list, parent, false)
                }
            }
        )
    }

    override fun getItemViewType(position: Int): Int = viewType.viewType

    override fun getItemCount(): Int = currencyList.size

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val drawableRes = flagRetriever.getDrawable(currencyList[position].name ?: "")
        holder.countryFlag.run {
            setImageDrawable(resources.getDrawable(drawableRes, null))
        }
        holder.rate.text = getPrice(position)
        holder.currency.text = currencyList[position].name
    }

    private fun getPrice(position: Int): String {
        val price = (currentRate * ((currencyList[position].rate ?: 0f) / conversionRate))
        return if (price > BASE_LONG_CONVERSION) {
            formatter.format(price.toLong())
        } else {
            price.toString()
        }
    }

    enum class ListViewType(val viewType: Int) {
        GRID(1),
        LIST(2)
    }

    companion object {
        private const val BASE_LONG_CONVERSION = 1000
        private const val BASE_CURRENT_RATE = 1f
        private const val BASE_CONVERSION_RATE = 1f
        private const val PATTERN = "#,###,###"
    }
}

class CurrencyViewHolder(containerView: View) : RecyclerView.ViewHolder(containerView) {
    val currency: TextView = containerView.currency
    val countryFlag: ImageView = containerView.flag
    val rate: TextView = containerView.rate
}