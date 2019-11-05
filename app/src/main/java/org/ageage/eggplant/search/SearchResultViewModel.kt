package org.ageage.eggplant.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.MinimumBookmarkCount
import org.ageage.eggplant.common.enums.SearchTarget
import org.ageage.eggplant.common.enums.SortType
import org.ageage.eggplant.common.model.SearchFilterOption
import org.ageage.eggplant.common.repository.FeedRepository
import org.ageage.eggplant.common.schedulerprovider.BaseSchedulerProvider

class SearchResultViewModel(
    private val repository: FeedRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val disposable = CompositeDisposable()

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

    var searchFilterOption = SearchFilterOption(
        SortType.RECENT,
        SearchTarget.TEXT,
        MinimumBookmarkCount.ONE,
        true
    )

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun search(forceLoad: Boolean = false) {
        if (isAlreadyLoaded && !forceLoad) {
            return
        }

        repository
            .search(keyword, searchFilterOption, 1)
            .doOnSubscribe {
                _status.postValue(Status.Loading)
            }
            .doOnSuccess {
                _status.postValue(Status.Success)
            }
            .doOnError {
                _status.postValue(Status.Error(it))
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { itemList ->
                    currentPageIndex = 1
                    _items.postValue(itemList)
                    isAlreadyLoaded = true
                },
                {
                }
            )
            .addTo(disposable)
    }

    fun loadNextPage() {
        repository
            .search(keyword, searchFilterOption, currentPageIndex + 1)
            .doOnSubscribe {
                _status.postValue(Status.LoadingNextPage)
            }
            .doOnSuccess {
                _status.postValue(Status.Success)
            }
            .doOnError {
                _status.postValue(Status.ErrorLoadNextPage(it))
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { itemList ->
                    if (itemList.isEmpty()) {
                        _status.postValue(Status.ReachedLastPage)
                    } else {
                        currentPageIndex += 1
                        _items.postValue(items.value?.union(itemList)?.toList() ?: itemList)
                    }
                },
                {
                }
            )
            .addTo(disposable)
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
