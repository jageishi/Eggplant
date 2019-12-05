package org.ageage.eggplant.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.ageage.eggplant.common.repository.LoginRepository
import org.ageage.eggplant.common.schedulerprovider.BaseSchedulerProvider

class LoginViewModel(
    private val repository: LoginRepository,
    private val schedulerProvider: BaseSchedulerProvider
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _statusFetchAuthorizationUrl = MutableLiveData<Status<String>>()
    val statusFetchAuthorizationUrl: LiveData<Status<String>>
        get() = _statusFetchAuthorizationUrl

    private val _statusLogin = MutableLiveData<Status<String>>()
    val statusLogin: LiveData<Status<String>>
        get() = _statusLogin

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun fetchAuthorizationUrl() {
        repository.fetchAuthorizationUrl()
            .doOnSubscribe {
                _statusFetchAuthorizationUrl.postValue(Status.Loading())
            }
            .doOnSuccess {
                _statusFetchAuthorizationUrl.postValue(Status.Success(it))
            }
            .doOnError {
                _statusFetchAuthorizationUrl.postValue(Status.Error(it))
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                {},
                {}
            )
            .addTo(disposable)
    }

    fun login(oAuthVerifier: String) {
        repository.fetchAccessToken(oAuthVerifier)
            .doOnSubscribe {
                _statusLogin.postValue(Status.Loading())
            }
            .doOnSuccess {
                _statusLogin.postValue(Status.Success(""))
            }
            .doOnError {
                _statusLogin.postValue(Status.Error(it))
            }
            .subscribeOn(schedulerProvider.io())
            .observeOn(schedulerProvider.ui())
            .subscribe(
                {},
                {}
            )
            .addTo(disposable)
    }

    sealed class Status<T>(
        val data: T? = null
    ) {
        class Loading<T>(data: T? = null) : Status<T>(data)
        class Success<T>(data: T) : Status<T>(data)
        class Error<T>(val error: Throwable) : Status<T>()
    }
}