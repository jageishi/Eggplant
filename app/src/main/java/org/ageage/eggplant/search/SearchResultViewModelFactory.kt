package org.ageage.eggplant.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import org.ageage.eggplant.common.repository.FeedRepository

class SearchResultViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != SearchResultViewModel::class.java) {
            throw IllegalArgumentException("Illegal ViewModel class.")
        }

        return SearchResultViewModel(
            FeedRepository()
        ) as T
    }
}