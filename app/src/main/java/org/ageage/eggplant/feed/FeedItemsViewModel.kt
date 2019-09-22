package org.ageage.eggplant.feed

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.ageage.eggplant.common.api.response.Item
import org.ageage.eggplant.common.enums.Category
import org.ageage.eggplant.common.enums.Mode
import org.ageage.eggplant.common.repository.FeedRepository

class FeedItemsViewModel(
    private val repository: FeedRepository
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

        _isLoading.value = true
        repository
            .fetchRss(mode, category)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { itemList ->
                    _items.value = itemList
                    _isLoading.value = false
                    isAlreadyLoaded = true
                }, {
                    _isLoading.value = false
                }
            )
    }
}