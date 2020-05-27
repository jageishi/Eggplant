package org.ageage.eggplant.feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.ageage.eggplant.repository.api.response.Item

class FeedItemsViewModel(
    private val repository: org.ageage.eggplant.repository.FeedRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var isAlreadyLoaded = false

    fun loadRss(
        mode: org.ageage.eggplant.repository.enums.Mode,
        category: org.ageage.eggplant.repository.enums.Category,
        forceLoad: Boolean = false
    ) {
        if (isAlreadyLoaded && !forceLoad) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val feedItems = repository.fetchRss(mode, category)
                _items.value = feedItems
                isAlreadyLoaded = true
            } catch (e: Throwable) {
                println(e.message)
            } finally {
                _isLoading.value = false

            }
        }
    }
}