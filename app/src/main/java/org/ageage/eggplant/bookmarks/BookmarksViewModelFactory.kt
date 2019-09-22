package org.ageage.eggplant.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ageage.eggplant.common.repository.BookmarkRepository

class BookmarksViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != BookmarksViewModel::class.java) {
            throw IllegalArgumentException("Illegal ViewModel class.")
        }

        return BookmarksViewModel(BookmarkRepository()) as T
    }
}