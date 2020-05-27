package org.ageage.eggplant.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.ageage.eggplant.common.schedulerprovider.BaseSchedulerProvider
import org.ageage.eggplant.repository.BookmarkRepository

class BookmarksViewModel(
    private val repository: BookmarkRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading

    private val _bookmarks = MutableLiveData<List<org.ageage.eggplant.repository.model.Bookmark>>()
    val bookmarks: LiveData<List<org.ageage.eggplant.repository.model.Bookmark>>
        get() = _bookmarks

    private var isAlreadyLoaded = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun loadBookmarks(
        url: String,
        sortType: org.ageage.eggplant.repository.enums.SortType,
        forceLoad: Boolean = false
    ) {
        if (isAlreadyLoaded && !forceLoad) {
            return
        }

        _isLoading.value = true
        repository
            .fetchBookmarks(url)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe({ bookmarkList ->
                val sortedBookmarks = when (sortType) {
                    org.ageage.eggplant.repository.enums.SortType.POPULAR -> {
                        bookmarkList
                            .filter { bookmark -> bookmark.comment.isNotEmpty() }
                            .sortedByDescending { bookmark -> bookmark.yellowStarNumber }
                            .take(10)
                    }
                    org.ageage.eggplant.repository.enums.SortType.RECENT -> {
                        bookmarkList
                            .filter { bookmark -> bookmark.comment.isNotEmpty() }
                            .sortedByDescending { bookmark -> bookmark.timeStamp }
                    }
                }

                _bookmarks.value = sortedBookmarks
                _isLoading.value = false
                isAlreadyLoaded = true

            }, {
                _isLoading.value = false
            })
            .addTo(disposable)
    }
}