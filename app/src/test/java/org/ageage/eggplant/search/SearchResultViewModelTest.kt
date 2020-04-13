package org.ageage.eggplant.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.MinimumBookmarkCount
import org.ageage.eggplant.common.enums.SearchTarget
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.model.SearchFilterOption
import org.ageage.eggplant.common.repository.FeedRepository
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.After
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
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun search_onSuccess() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)
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
    }

    @Test
    fun search_onError() {
        runBlocking {
            val exception = RuntimeException()
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doThrow exception
            }
            val viewModel = SearchResultViewModel(mockRepository)
            val orderedVerifier = inOrder(mockRepository, itemObserver, statusObserver)

            viewModel.items.observeForever(itemObserver)
            viewModel.status.observeForever(statusObserver)

            viewModel.search()

            orderedVerifier.verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 1)
            orderedVerifier.verify(statusObserver, times(1))
                .onChanged(SearchResultViewModel.Status.Loading)
            orderedVerifier.verify(statusObserver, times(1))
                .onChanged(SearchResultViewModel.Status.Error(exception))
            orderedVerifier.verify(itemObserver, never())
                .onChanged(any())
        }
    }

    @Test
    fun search_emptyItem() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doReturn listOf()
            }
            val viewModel = SearchResultViewModel(mockRepository)

            viewModel.isEmptyItems.observeForever(emptyItemsObserver)

            viewModel.search()

            verify(emptyItemsObserver, times(1))
                .onChanged(true)
        }
    }

    @Test
    fun search_twice() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)

            viewModel.search()
            viewModel.search()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 1)
        }
    }

    @Test
    fun search_twice_forcibly() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)

            viewModel.search()
            viewModel.search(true)

            verify(mockRepository, times(2))
                .search("", defaultSearchFilterOption, 1)
        }
    }

    @Test
    fun loadNextPage_onSuccess() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 2) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)
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
    }

    @Test
    fun loadNextPage_onError() {
        runBlocking {
            val exception = RuntimeException()
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 2) } doThrow exception
            }
            val viewModel = SearchResultViewModel(mockRepository)
            val orderedVerifier = inOrder(itemObserver, statusObserver)

            viewModel.items.observeForever(itemObserver)
            viewModel.status.observeForever(statusObserver)

            viewModel.loadNextPage()

            orderedVerifier.verify(statusObserver, times(1))
                .onChanged(SearchResultViewModel.Status.LoadingNextPage)
            orderedVerifier.verify(statusObserver, times(1))
                .onChanged(SearchResultViewModel.Status.ErrorLoadNextPage(exception))
            orderedVerifier.verify(itemObserver, never())
                .onChanged(any())

            assertThat(viewModel.hasLoadNextPageError)
                .isTrue()
        }
    }

    @Test
    fun loadNextPage_emptyItems() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 2) } doReturn listOf()
            }
            val viewModel = SearchResultViewModel(mockRepository)
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
    }

    @Test
    fun loadNextPage_twice() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 2) } doReturn fakeItems
                onBlocking { search("", defaultSearchFilterOption, 3) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)

            viewModel.loadNextPage()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 2)

            viewModel.loadNextPage()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 3)
        }
    }

    @Test
    fun search_afterLoadNextPage() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doReturn fakeItems
                onBlocking { search("", defaultSearchFilterOption, 2) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)

            viewModel.loadNextPage()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 2)

            viewModel.search()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 1)
        }
    }

    @Test
    fun loadNextPage_afterSearch() {
        runBlocking {
            val mockRepository = mock<FeedRepository> {
                onBlocking { search("", defaultSearchFilterOption, 1) } doReturn fakeItems
                onBlocking { search("", defaultSearchFilterOption, 2) } doReturn fakeItems
            }
            val viewModel = SearchResultViewModel(mockRepository)

            viewModel.search()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 1)

            viewModel.loadNextPage()

            verify(mockRepository, times(1))
                .search("", defaultSearchFilterOption, 2)
        }
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