package org.ageage.eggplant.bookmarks

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.ageage.eggplant.common.api.response.Bookmark
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.repository.BookmarkRepository
import java.text.SimpleDateFormat
import java.util.*

class BookmarksViewModel(
    private val repository: BookmarkRepository
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
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ bookmarkList ->
                val sortedBookmarks = when (sortType) {
                    SortType.POPULAR -> {
                        bookmarkList
                            .filter { bookmark -> bookmark.comment.isNotEmpty() }
                            .sortedByDescending { bookmark -> bookmark.entry?.stars?.size ?: 0 }
                            .take(10)
                    }
                    SortType.RECENT -> {
                        bookmarkList
                            .filter { bookmark -> bookmark.comment.isNotEmpty() }
                            .sortedByDescending { bookmark ->
                                SimpleDateFormat(
                                    "yyyy/MM/dd HH:mm",
                                    Locale.US
                                ).parse(bookmark.timestamp)
                            }
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