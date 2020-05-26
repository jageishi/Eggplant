package org.ageage.eggplant.bookmarks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ageage.eggplant.common.schedulerprovider.SchedulerProvider
import org.ageage.eggplant.data.repository.BookmarkRepository

class BookmarksViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != BookmarksViewModel::class.java) {
            throw IllegalArgumentException("Illegal ViewModel class.")
        }

        return BookmarksViewModel(
            BookmarkRepository(),
            SchedulerProvider()
        ) as T
    }
}