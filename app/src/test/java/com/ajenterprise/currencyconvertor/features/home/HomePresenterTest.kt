package com.ajenterprise.currencyconvertor.features.home

import com.ajenterprise.currencyconvertor.features.home.HomePresenter
import com.ajenterprise.currencyconvertor.features.home.di.HomeContract
import com.ajenterprise.currencyconvertor.room.CurrencyRate
import com.ajenterprise.currencyconvertor.utils.DispatchersFactory
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.spy
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class HomePresenterTest {
    private val mockViewProxy: HomeContract.ViewProxy = mock()
    private val dispatcher = object :
        DispatchersFactory {
        private val testDispatcher = TestCoroutineDispatcher()
        override val io: CoroutineDispatcher
            get() = testDispatcher
        override val default: CoroutineDispatcher
            get() = testDispatcher
        override val main: CoroutineDispatcher
            get() = testDispatcher
    }
    private val mockRepository: HomeContract.Repository = mock()
    private val mockLocalDataList: List<CurrencyRate> = listOf(
        CurrencyRate(0, "", 0F)
    )
    private val mockRemoteDataList: List<CurrencyRate> = listOf(
        CurrencyRate(1, "", 0F)
    )
    private val selectedPosition = 1

    private lateinit var presenter: HomePresenter

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(dispatcher.main)

        presenter = spy(
            HomePresenter(
                mockViewProxy,
                mockRepository,
                dispatcher
            )
        )
        reset(mockRepository, mockViewProxy, presenter)

        whenever(presenter.selectedPosition).thenReturn(selectedPosition)
    }

    @ExperimentalCoroutinesApi
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onCreateActivity() {
        doNothing().whenever(presenter).fetchAndPopulate()
        presenter.onCreateActivity()

        verify(presenter).fetchAndPopulate()
        verify(mockViewProxy).setup(presenter::fetchAndPopulate, presenter::onItemChange)
    }

    @Test
    fun `fetchAndPopulate if time less than 30 mins`() = runBlocking {
        whenever(mockRepository.getLocalCurrencyList()).thenReturn(mockLocalDataList)
        whenever(mockRepository.getRemoteCurrencyList()).thenReturn(mockRemoteDataList)
        whenever(mockRepository.getLatestTimestampOnData()).thenReturn(System.currentTimeMillis())

        presenter.fetchAndPopulate()

        verify(mockViewProxy).hideErrorScreen()
        verify(mockViewProxy).showLoading()
        verify(mockViewProxy).hideLoading()
        assertEquals(mockLocalDataList, presenter.list)
        verify(mockViewProxy).updateList(mockLocalDataList)
        verify(mockRepository).getLocalCurrencyList()
        verify(mockViewProxy).selectPosition(selectedPosition)
        verify(presenter).onItemChange(selectedPosition)
    }

    @Test
    fun `fetchAndPopulate if time more than 30 mins`() = runBlocking {
        whenever(mockRepository.getLocalCurrencyList()).thenReturn(mockLocalDataList)
        whenever(mockRepository.getRemoteCurrencyList()).thenReturn(mockRemoteDataList)
        whenever(mockRepository.getLatestTimestampOnData()).thenReturn(0)
        presenter.fetchAndPopulate()

        verify(mockViewProxy).hideErrorScreen()
        verify(mockViewProxy).showLoading()
        verify(mockViewProxy).hideLoading()
        assertEquals(mockRemoteDataList, presenter.list)
        verify(mockRepository).getRemoteCurrencyList()
        verify(mockViewProxy).updateList(mockRemoteDataList)
        verify(mockViewProxy).selectPosition(selectedPosition)
        verify(presenter).onItemChange(selectedPosition)
    }

    @Test
    fun `fetchAndPopulate if Error`() = runBlocking {
        whenever(mockRepository.getLocalCurrencyList()).thenReturn(null)
        whenever(mockRepository.getRemoteCurrencyList()).thenReturn(null)

        presenter.fetchAndPopulate()

        verify(mockViewProxy).hideErrorScreen()
        verify(mockViewProxy).showLoading()
        verify(mockViewProxy).hideLoading()
        verify(mockRepository).getRemoteCurrencyList()
        verify(mockViewProxy).showErrorToast()
        verify(mockViewProxy).showErrorScreen()
        verify(mockViewProxy, never()).updateList(mockRemoteDataList)
        verify(mockViewProxy, never()).selectPosition(selectedPosition)
        verify(presenter, never()).onItemChange(selectedPosition)
    }
}
