package org.ageage.eggplant.feed

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.common.repository.FeedRepository
import org.ageage.eggplant.common.schedulerprovider.BaseSchedulerProvider

class FeedItemsViewModel(
    private val repository: FeedRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val _items = MutableLiveData<List<Item>>()
    val items: LiveData<List<Item>>
        get() = _items

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var isAlreadyLoaded = false

    @SuppressLint("CheckResult")
    fun loadRss(mode: Mode, category: Category, forceLoad: Boolean = false) {
        if (isAlreadyLoaded && !forceLoad) {
            return
        }

        _isLoading.postValue(true)
        repository
            .fetchRss(mode, category)
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                { itemList ->
                    _items.postValue(itemList)
                    _isLoading.postValue(false)
                    isAlreadyLoaded = true
                }, {
                    _isLoading.postValue(false)
                }
            )
    }
}