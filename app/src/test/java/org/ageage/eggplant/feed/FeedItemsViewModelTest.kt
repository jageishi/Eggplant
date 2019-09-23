package org.ageage.eggplant.feed

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.common.repository.FeedRepository
import org.ageage.eggplant.common.schedulerprovider.TrampolineSchedulerProvider
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.io.IOException

class FeedItemsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var itemObserver: Observer<List<Item>>

    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    private val mode = Mode.ENTRY_LIST
    private val category = Category.OVERALL

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun loadRss_onSuccess() {
        val mockRepository = mock<FeedRepository> {
            on { fetchRss(mode, category) } doReturn Single.just(fakeItems)
        }

        val viewModel = FeedItemsViewModel(mockRepository, TrampolineSchedulerProvider())

        val orderedVerifier = inOrder(itemObserver, loadingObserver)

        viewModel.items.observeForever(itemObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.loadRss(mode, category)

        orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
        orderedVerifier.verify(itemObserver, times(1)).onChanged(fakeItems)
        orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)
    }

    @Test
    fun loadRss_onError() {
        val mockRepository = mock<FeedRepository> {
            on { fetchRss(mode, category) } doReturn Single.error(IOException())
        }

        val viewModel = FeedItemsViewModel(mockRepository, TrampolineSchedulerProvider())

        val orderedVerifier = inOrder(loadingObserver)

        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.loadRss(mode, category)

        orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
        orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)
    }

    @Test
    fun loadRss_twice() {
        val mockRepository = mock<FeedRepository> {
            on { fetchRss(mode, category) } doReturn Single.just(fakeItems)
        }

        val viewModel = FeedItemsViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.loadRss(mode, category)
        viewModel.loadRss(mode, category)

        verify(mockRepository, times(1)).fetchRss(mode, category)
    }

    @Test
    fun loadRss_twice_forcibly() {
        val mockRepository = mock<FeedRepository> {
            on { fetchRss(mode, category) } doReturn Single.just(fakeItems)
        }

        val viewModel = FeedItemsViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.loadRss(mode, category)
        viewModel.loadRss(mode, category, true)

        verify(mockRepository, times(2)).fetchRss(mode, category)
    }

    companion object {
        val fakeItems = listOf(
            Item("1", "1", "1", "1", "1", "1"),
            Item("2", "2", "2", "2", "2", "2"),
            Item("3", "3", "3", "3", "3", "3")
        )
    }
}