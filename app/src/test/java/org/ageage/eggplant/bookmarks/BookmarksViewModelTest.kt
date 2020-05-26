package org.ageage.eggplant.bookmarks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Observable
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.schedulerprovider.TrampolineSchedulerProvider
import org.ageage.eggplant.data.repository.BookmarkRepository
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.text.SimpleDateFormat
import java.util.*

class BookmarksViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var bookmarksObserver: Observer<List<org.ageage.eggplant.data.repository.model.Bookmark>>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    private val url = ""

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun loodBookmarks_onSuccess_popular() {
        val mockRepository = mock<BookmarkRepository> {
            on { fetchBookmarks(url) } doReturn Observable.just(fakeBookmarks)
        }

        val viewModel = BookmarksViewModel(mockRepository, TrampolineSchedulerProvider())

        val orderedVerifier = inOrder(bookmarksObserver, loadingObserver)

        viewModel.bookmarks.observeForever(bookmarksObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.loadBookmarks(url, SortType.POPULAR)

        orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
        orderedVerifier.verify(bookmarksObserver, times(1)).onChanged(any())
        orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)

        assertThat(viewModel.bookmarks.value!!.size, equalTo(10))
    }

    @Test
    fun loadBookmarks_onSuccess_recent() {
        val mockRepository = mock<BookmarkRepository> {
            on { fetchBookmarks(url) } doReturn Observable.just(fakeBookmarks)
        }

        val viewModel = BookmarksViewModel(mockRepository, TrampolineSchedulerProvider())

        val orderedVerifier = inOrder(bookmarksObserver, loadingObserver)

        viewModel.bookmarks.observeForever(bookmarksObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.loadBookmarks(url, SortType.RECENT)

        orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
        orderedVerifier.verify(bookmarksObserver, times(1)).onChanged(any())
        orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)

        assertThat(viewModel.bookmarks.value!!.size, equalTo(100))
    }

    @Test
    fun loadBookmarks_onError() {
        val mockRepository = mock<BookmarkRepository> {
            on { fetchBookmarks(url) } doReturn Observable.error(Throwable())
        }

        val viewModel = BookmarksViewModel(mockRepository, TrampolineSchedulerProvider())

        val orderedVerifier = inOrder(bookmarksObserver, loadingObserver)

        viewModel.bookmarks.observeForever(bookmarksObserver)
        viewModel.isLoading.observeForever(loadingObserver)

        viewModel.loadBookmarks(url, SortType.POPULAR)

        orderedVerifier.verify(loadingObserver, times(1)).onChanged(true)
        orderedVerifier.verify(bookmarksObserver, never()).onChanged(any())
        orderedVerifier.verify(loadingObserver, times(1)).onChanged(false)
    }

    @Test
    fun loadBookmarks_twice() {
        val mockRepository = mock<BookmarkRepository> {
            on { fetchBookmarks(url) } doReturn Observable.just(fakeBookmarks)
        }

        val viewModel = BookmarksViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.loadBookmarks(url, SortType.POPULAR)
        viewModel.loadBookmarks(url, SortType.POPULAR)

        verify(mockRepository, times(1)).fetchBookmarks(url)
    }


    @Test
    fun loadBookmarks_twice_forcibly() {
        val mockRepository = mock<BookmarkRepository> {
            on { fetchBookmarks(url) } doReturn Observable.just(fakeBookmarks)
        }

        val viewModel = BookmarksViewModel(mockRepository, TrampolineSchedulerProvider())

        viewModel.loadBookmarks(url, SortType.POPULAR)
        viewModel.loadBookmarks(url, SortType.POPULAR, true)

        verify(mockRepository, times(2)).fetchBookmarks(url)
    }

    companion object {

        val fakeBookmarks =
            (1..100).map {
                org.ageage.eggplant.data.repository.model.Bookmark(
                    "user$it",
                    emptyList(),
                    SimpleDateFormat(
                        "yyyy/MM/dd HH:mm",
                        Locale.US
                    ).parse("2019/${if (it < 10) "0$it" else it}/01 12:00"),
                    "comment",
                    it
                )

            }

    }
}