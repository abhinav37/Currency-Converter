package com.ajenterprise.paypaybaymax.features.home

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ajenterprise.paypaybaymax.features.home.adapter.CurrencyAdapter
import com.ajenterprise.paypaybaymax.room.CurrencyRate
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.android.synthetic.main.activity_home.view.currency_spinner
import kotlinx.android.synthetic.main.activity_home.view.error_screen
import kotlinx.android.synthetic.main.activity_home.view.grid_list_button
import kotlinx.android.synthetic.main.activity_home.view.progress_bar
import kotlinx.android.synthetic.main.activity_home.view.recycler_view
import kotlinx.android.synthetic.main.activity_home.view.refresh_button
import kotlinx.android.synthetic.main.activity_home.view.user_rate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HomeViewProxyTest {
    private val mockLocalDataList: List<CurrencyRate> = listOf(CurrencyRate(0, "", 0F))
    private val currencySpinnerAdapter: ArrayAdapter<String> = mock()
    private val currencyAdapter: CurrencyAdapter = mock()
    private val toast: Toast = mock()
    private val containerView: View = mock()
    private val recyclerView: RecyclerView = mock()
    private val currencySpinner: Spinner = mock()
    private val userRate: EditText = mock()
    private val gridListButton: ImageButton = mock()
    private val refreshButton: Button = mock()
    private val progressBar: ProgressBar = mock()
    private val errorScreen: ConstraintLayout = mock()
    private val mockListenerFactory: ListenerFactory = mock()
    private val mockGridLayoutManager: GridLayoutManager = mock()
    private val mockListLayoutManager: LinearLayoutManager = mock()
    private val mockOnTextChangeListener: (text: CharSequence?, start: Int, end: Int, count: Int) -> Unit =
        mock()
    private val mockOnItemSelectedListener: AdapterView.OnItemSelectedListener = mock()

    private lateinit var viewProxy: HomeViewProxy

    @BeforeEach
    fun setUp() {
        whenever(mockListenerFactory.getOnTextChanged(any())).thenReturn(mockOnTextChangeListener)
        whenever(mockListenerFactory.getOnItemSelectedListener(any())).thenReturn(
            mockOnItemSelectedListener
        )
        mockListenerFactory
        viewProxy = spy(
            HomeViewProxy(
                currencySpinnerAdapter,
                currencyAdapter,
                mockGridLayoutManager,
                mockListLayoutManager,
                mockListenerFactory,
                toast,
                containerView
            )
        )
        reset(viewProxy, currencySpinnerAdapter, currencyAdapter, toast, containerView)

        whenever(containerView.recycler_view).thenReturn(recyclerView)
        whenever(containerView.currency_spinner).thenReturn(currencySpinner)
        whenever(containerView.user_rate).thenReturn(userRate)
        whenever(containerView.grid_list_button).thenReturn(gridListButton)
        whenever(containerView.refresh_button).thenReturn(refreshButton)
        whenever(containerView.progress_bar).thenReturn(progressBar)
        whenever(containerView.error_screen).thenReturn(errorScreen)
    }

    @Test
    fun `setup view`() {
        val mockAction: () -> Unit = mock()
        val mockOnItemChange: (Int) -> Unit = mock()
        viewProxy.setup(mockAction, mockOnItemChange)

        verify(recyclerView).layoutManager = any<GridLayoutManager>()
        verify(currencyAdapter).setListType(viewProxy.viewType)
        verify(recyclerView).adapter = currencyAdapter
        verify(currencySpinner).adapter = currencySpinnerAdapter
        verify(currencySpinner).onItemSelectedListener = mockOnItemSelectedListener
        verify(gridListButton).setOnClickListener(any())
        verify(refreshButton).setOnClickListener(any())
    }

    @Test
    fun updateList() {
        viewProxy.updateList(mockLocalDataList)

        verify(currencyAdapter).setAdapterAndRefresh(mockLocalDataList)
        verify(currencySpinnerAdapter).clear()
        verify(currencySpinnerAdapter).addAll(any<List<String>>())
        verify(currencySpinnerAdapter).notifyDataSetChanged()
    }

    @Test
    fun updateRateText() {
        val rate = 8f
        viewProxy.updateRateText(rate)

        verify(currencyAdapter).setCurrentRate(rate)
    }

    @Test
    fun updateConversionRate() {
        val rate = 8f
        viewProxy.updateConversionRate(rate)

        verify(currencyAdapter).setConversionRate(rate)
    }

    @Test
    fun selectPosition() {
        val selectedPosition = 0
        viewProxy.selectPosition(selectedPosition)

        verify(currencySpinner).setSelection(selectedPosition)
    }

    @Test
    fun showLoading() {
        viewProxy.showLoading()

        verify(progressBar).isVisible = true
    }

    @Test
    fun hideLoading() {
        viewProxy.hideLoading()

        verify(progressBar).isVisible = false
    }

    @Test
    fun showErrorScreen() {
        viewProxy.showErrorScreen()

        verify(errorScreen).isVisible = true
        verify(recyclerView).isVisible = false
    }

    @Test
    fun hideErrorScreen() {
        viewProxy.hideErrorScreen()

        verify(errorScreen).isVisible = false
        verify(recyclerView).isVisible = true
    }
}
