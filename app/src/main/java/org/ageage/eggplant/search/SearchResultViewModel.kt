package org.ageage.eggplant.search

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import org.ageage.eggplant.repository.api.response.Item
import org.ageage.eggplant.repository.enums.MinimumBookmarkCount
import org.ageage.eggplant.repository.enums.SearchTarget
import org.ageage.eggplant.repository.enums.SortType

class SearchResultViewModel(
    private val repository: org.ageage.eggplant.repository.FeedRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val isReachedLastPage: Boolean
        get() = status.value?.isReachedLastPage ?: false

    val isLoadingNextPage: Boolean
        get() = status.value?.isLoadingNextPage ?: false

    val hasLoadNextPageError: Boolean
        get() = status.value?.hasLoadNextPageError ?: false

    val isEmptyItems: LiveData<Boolean> = Transformations.map(_items) { items ->
        items.isEmpty()
    }

    private var currentPageIndex = 1
    private var isAlreadyLoaded = false

    var keyword: String = ""

    var searchFilterOption = org.ageage.eggplant.repository.model.SearchFilterOption(
        org.ageage.eggplant.repository.enums.SortType.RECENT,
        org.ageage.eggplant.repository.enums.SearchTarget.TEXT,
        org.ageage.eggplant.repository.enums.MinimumBookmarkCount.ONE,
        true
    )

    fun search(forceLoad: Boolean = false) {
        if (isAlreadyLoaded && !forceLoad) {
            return
        }

        viewModelScope.launch {
            _status.value = Status.Loading
            try {
                val feedItems = repository.search(keyword, searchFilterOption, 1)
                _status.value = Status.Success
                currentPageIndex = 1
                _items.value = feedItems
                isAlreadyLoaded = true
            } catch (e: Throwable) {
                _status.value = Status.Error(e)
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            _status.value = Status.LoadingNextPage
            try {
                val feedItems = repository.search(
                    keyword,
                    searchFilterOption,
                    currentPageIndex + 1
                )

                _status.value = Status.Success

                if (feedItems.isEmpty()) {
                    _status.value = Status.ReachedLastPage
                } else {
                    currentPageIndex += 1
                    _items.value = items.value?.union(feedItems)?.toList() ?: feedItems
                }

            } catch (e: Throwable) {
                _status.value = Status.ErrorLoadNextPage(e)
            }
        }
    }

    sealed class Status {
        object Loading : Status()
        object LoadingNextPage : Status()
        object Success : Status()
        object ReachedLastPage : Status()
        data class Error(val error: Throwable) : Status()
        data class ErrorLoadNextPage(val error: Throwable) : Status()

        val isLoading: Boolean
            get() = this is Loading

        val isLoadingNextPage: Boolean
            get() = this is LoadingNextPage

        val isReachedLastPage: Boolean
            get() = this is ReachedLastPage

        val hasLoadNextPageError: Boolean
            get() = this is ErrorLoadNextPage
    }
}
