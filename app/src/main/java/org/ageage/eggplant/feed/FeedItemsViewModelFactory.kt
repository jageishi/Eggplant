package org.ageage.eggplant.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FeedItemsViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass != FeedItemsViewModel::class.java) {
            throw IllegalArgumentException("Illegal ViewModel class.")
        }

        return FeedItemsViewModel(
            org.ageage.eggplant.repository.FeedRepository()
        ) as T
    }
}