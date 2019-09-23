package org.ageage.eggplant.bookmarks

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.model.Bookmark
import org.ageage.eggplant.common.repository.BookmarkRepository
import org.ageage.eggplant.common.schedulerprovider.BaseSchedulerProvider

class BookmarksViewModel(
    private val repository: BookmarkRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading

    private val _bookmarks = MutableLiveData<List<Bookmark>>()
    val bookmarks: LiveData<List<Bookmark>>
        get() = _bookmarks

    private var isAlreadyLoaded = false

    @SuppressLint("CheckResult")
    fun loadBookmarks(url: String, sortType: SortType, forceLoad: Boolean = false) {
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
                    SortType.POPULAR -> {
                        bookmarkList
                            .filter { bookmark -> bookmark.comment.isNotEmpty() }
                            .sortedByDescending { bookmark -> bookmark.yellowStarNumber }
                            .take(10)
                    }
                    SortType.RECENT -> {
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
    }
}