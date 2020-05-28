package org.ageage.eggplant.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.data.api.response.Item
import org.ageage.eggplant.data.repository.FeedRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class FeedItemsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var itemObserver: Observer<List<Item>>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    private val mode = Mode.ENTRY_LIST
    private val category = Category.OVERALL

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadRss_onSuccess() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { fetchRss(mode, category) } doReturn fakeItems
            }

            val viewModel = FeedItemsViewModel(mockRepository)

            val orderedVerifier = inOrder(itemObserver, loadingObserver)

            viewModel.items.observeForever(itemObserver)
            viewModel.isLoading.observeForever(loadingObserver)

            viewModel.loadRss(mode, category)

            orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
            orderedVerifier.verify(itemObserver, times(1)).onChanged(fakeItems)
            orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)
        }
    }

    @Test
    fun loadRss_onError() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { fetchRss(mode, category) } doThrow RuntimeException()
            }

            val viewModel = FeedItemsViewModel(mockRepository)

            val orderedVerifier = inOrder(itemObserver, loadingObserver)

            viewModel.items.observeForever(itemObserver)
            viewModel.isLoading.observeForever(loadingObserver)

            viewModel.loadRss(mode, category)

            orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
            orderedVerifier.verify(itemObserver, never()).onChanged(fakeItems)
            orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)
        }
    }

    @Test
    fun loadRss_twice() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { fetchRss(mode, category) } doReturn fakeItems
            }

            val viewModel = FeedItemsViewModel(mockRepository)

            viewModel.loadRss(mode, category)
            viewModel.loadRss(mode, category)

            verify(mockRepository, times(1)).fetchRss(mode, category)
        }
    }

    @Test
    fun loadRss_twice_forcibly() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { fetchRss(mode, category) } doReturn fakeItems
            }

            val viewModel = FeedItemsViewModel(mockRepository)

            viewModel.loadRss(mode, category)
            viewModel.loadRss(mode, category, true)

            verify(mockRepository, times(2)).fetchRss(mode, category)
        }
    }

    companion object {
        val fakeItems = listOf(
            Item("1", "1", "1", "1", "1", "1"),
            Item("2", "2", "2", "2", "2", "2"),
            Item("3", "3", "3", "3", "3", "3")
        )
    }
}