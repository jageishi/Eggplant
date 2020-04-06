package org.ageage.eggplant.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.model.Bookmark
import org.ageage.eggplant.common.repository.BookmarkRepository

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

    fun loadBookmarks(url: String, sortType: SortType, forceLoad: Boolean = false) {
        if (isAlreadyLoaded && !forceLoad) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            repository
                .fetchBookmarks(url).let{ bookmarkList ->
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
                }
        }
    }
}