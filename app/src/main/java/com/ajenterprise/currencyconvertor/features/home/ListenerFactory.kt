package com.ajenterprise.currencyconvertor.features.home

import android.view.View
import android.widget.AdapterView
import javax.inject.Inject

class ListenerFactory @Inject constructor() {
    fun getOnItemSelectedListener(onItemChange: (Int) -> Unit) =
        object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View,
                position: Int,
                id: Long
            ) {
                onItemChange(position)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

    fun getOnTextChanged(action: (Float) -> Unit) = { text: CharSequence?, _: Int, _: Int, _: Int ->
        val rate = try {
            text.toString().toFloat()
        } catch (ex: Exception) {
            0f
        }
        action(rate)
    }

}

