package org.ageage.eggplant.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Single
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.MinimumBookmarkCount
import org.ageage.eggplant.common.enums.SearchTarget
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.model.SearchFilterOption
import org.ageage.eggplant.common.repository.FeedRepository
import org.ageage.eggplant.common.schedulerprovider.TrampolineSchedulerProvider
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class SearchResultViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var itemObserver: Observer<List<Item>>

    @Mock
    private lateinit var statusObserver: Observer<SearchResultViewModel.Status>

    @Mock
    private lateinit var emptyItemsObserver: Observer<Boolean>

    private var defaultSearchFilterOption = SearchFilterOption(
        SortType.RECENT,
        SearchTarget.TEXT,
        MinimumBookmarkCount.ONE,
        true
    )

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun search_onSuccess() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())
        val orderedVerifier = inOrder(itemObserver, statusObserver, emptyItemsObserver)

        viewModel.items.observeForever(itemObserver)
        viewModel.status.observeForever(statusObserver)
        viewModel.isEmptyItems.observeForever(emptyItemsObserver)

        viewModel.search()

        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.Loading)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.Success)
        orderedVerifier.verify(itemObserver, times(1))
            .onChanged(fakeItems)
        orderedVerifier.verify(emptyItemsObserver, times(1))
            .onChanged(false)
    }

    @Test
    fun search_onError() {
        val exception = Exception()
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.error(exception)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())
        val orderedVerifier = inOrder(mockRepository, itemObserver, statusObserver)

        viewModel.items.observeForever(itemObserver)
        viewModel.status.observeForever(statusObserver)

        viewModel.search()

        orderedVerifier.verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 1)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.Loading)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(any())
        orderedVerifier.verify(itemObserver, never())
            .onChanged(any())

        assertThat(viewModel.status.value)
            .isInstanceOf(SearchResultViewModel.Status.Error::class.java)
        assertThat((viewModel.status.value as SearchResultViewModel.Status.Error).error)
            .isEqualTo(exception)
    }

    @Test
    fun search_emptyItem() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.just(listOf())
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.isEmptyItems.observeForever(emptyItemsObserver)

        viewModel.search()

        verify(emptyItemsObserver, times(1))
            .onChanged(true)
    }

    @Test
    fun search_twice() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.search()
        viewModel.search()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 1)
    }

    @Test
    fun search_twice_forcibly() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.search()
        viewModel.search(true)

        verify(mockRepository, times(2))
            .search("", defaultSearchFilterOption, 1)
    }

    @Test
    fun loadNextPage_onSuccess() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 2) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())
        val statusObserverForLoadingNextPage = Observer<SearchResultViewModel.Status> {
            if (it is SearchResultViewModel.Status.LoadingNextPage) {
                assertThat(viewModel.isLoadingNextPage)
                    .isTrue()
            }
        }
        val orderedVerifier = inOrder(mockRepository, itemObserver, statusObserver)

        viewModel.items.observeForever(itemObserver)
        viewModel.status.observeForever(statusObserver)
        viewModel.status.observeForever(statusObserverForLoadingNextPage)

        viewModel.loadNextPage()

        orderedVerifier.verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 2)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.LoadingNextPage)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.Success)
        orderedVerifier.verify(itemObserver, times(1))
            .onChanged(fakeItems)

        assertThat(viewModel.isLoadingNextPage)
            .isFalse()
        assertThat(viewModel.isReachedLastPage)
            .isFalse()
        assertThat(viewModel.hasLoadNextPageError)
            .isFalse()
    }

    @Test
    fun loadNextPage_onError() {
        val exception = Exception()
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 2) } doReturn Single.error(exception)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())
        val orderedVerifier = inOrder(itemObserver, statusObserver)

        viewModel.items.observeForever(itemObserver)
        viewModel.status.observeForever(statusObserver)

        viewModel.loadNextPage()

        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.LoadingNextPage)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(any())
        orderedVerifier.verify(itemObserver, never())
            .onChanged(any())

        assertThat(viewModel.status.value)
            .isInstanceOf(SearchResultViewModel.Status.ErrorLoadNextPage::class.java)
        assertThat((viewModel.status.value as SearchResultViewModel.Status.ErrorLoadNextPage).error)
            .isEqualTo(exception)
        assertThat(viewModel.hasLoadNextPageError)
            .isTrue()
    }

    @Test
    fun loadNextPage_emptyItems() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 2) } doReturn Single.just(listOf())
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())
        val orderedVerifier = inOrder(itemObserver, statusObserver)

        viewModel.items.observeForever(itemObserver)
        viewModel.status.observeForever(statusObserver)

        viewModel.loadNextPage()

        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.LoadingNextPage)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.Success)
        orderedVerifier.verify(statusObserver, times(1))
            .onChanged(SearchResultViewModel.Status.ReachedLastPage)
        orderedVerifier.verify(itemObserver, never())
            .onChanged(any())

        assertThat(viewModel.isReachedLastPage)
            .isTrue()
    }

    @Test
    fun loadNextPage_twice() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 2) } doReturn Single.just(fakeItems)
            on { search("", defaultSearchFilterOption, 3) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.loadNextPage()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 2)

        viewModel.loadNextPage()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 3)
    }

    @Test
    fun search_afterLoadNextPage() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.just(fakeItems)
            on { search("", defaultSearchFilterOption, 2) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.loadNextPage()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 2)

        viewModel.search()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 1)
    }

    @Test
    fun loadNextPage_afterSearch() {
        val mockRepository = mock<FeedRepository> {
            on { search("", defaultSearchFilterOption, 1) } doReturn Single.just(fakeItems)
            on { search("", defaultSearchFilterOption, 2) } doReturn Single.just(fakeItems)
        }
        val viewModel = SearchResultViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.search()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 1)

        viewModel.loadNextPage()

        verify(mockRepository, times(1))
            .search("", defaultSearchFilterOption, 2)
    }

    @Test
    fun status_isLoading() {
        val status: SearchResultViewModel.Status = SearchResultViewModel.Status.Loading

        assertThat(status.isLoading).isTrue()
        assertThat(status.isLoadingNextPage).isFalse()
        assertThat(status.isReachedLastPage).isFalse()
        assertThat(status.hasLoadNextPageError).isFalse()
    }

    @Test
    fun status_isLoadingNextPage() {
        val status: SearchResultViewModel.Status = SearchResultViewModel.Status.LoadingNextPage

        assertThat(status.isLoading).isFalse()
        assertThat(status.isLoadingNextPage).isTrue()
        assertThat(status.isReachedLastPage).isFalse()
        assertThat(status.hasLoadNextPageError).isFalse()
    }

    @Test
    fun status_isReachedLastPage() {
        val status: SearchResultViewModel.Status = SearchResultViewModel.Status.ReachedLastPage

        assertThat(status.isLoading).isFalse()
        assertThat(status.isLoadingNextPage).isFalse()
        assertThat(status.isReachedLastPage).isTrue()
        assertThat(status.hasLoadNextPageError).isFalse()
    }


    @Test
    fun status_hasLoadNextPageError() {
        val status: SearchResultViewModel.Status =
            SearchResultViewModel.Status.ErrorLoadNextPage(Exception())

        assertThat(status.isLoading).isFalse()
        assertThat(status.isLoadingNextPage).isFalse()
        assertThat(status.isReachedLastPage).isFalse()
        assertThat(status.hasLoadNextPageError).isTrue()
    }

    companion object {
        private val fakeItems = listOf(
            Item("1", "1", "1", "1", "1", "1"),
            Item("2", "2", "2", "2", "2", "2"),
            Item("3", "3", "3", "3", "3", "3")
        )
    }
}