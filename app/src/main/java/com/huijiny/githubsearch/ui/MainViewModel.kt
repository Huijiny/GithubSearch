package com.huijiny.githubsearch.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huijiny.githubsearch.R
import com.huijiny.githubsearch.base.BaseViewModel
import com.huijiny.githubsearch.data.model.Repository
import com.huijiny.githubsearch.network.GithubInjection
import com.huijiny.githubsearch.repository.SearchRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException

class MainViewModel : BaseViewModel() {
    private val repository by lazy {
        SearchRepository(GithubInjection.provideSearchService())
    }

    private val _repositories = MutableLiveData<List<Repository>>()
    val repositories: LiveData<List<Repository>> get() = _repositories

    private val _error = MutableLiveData<Int>()
    val error: LiveData<Int> get() = _error

    fun searchRepository(query: String) {
        repository.searchRepository(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _repositories.value = it.repositories
            }, {
                _repositories.value = emptyList()
                getErrorString(it)
            })
            .addToDisposable()
    }

    private fun getErrorString(throwable: Throwable) {
        if (throwable is HttpException) {
            when (throwable.code()) {
                NOT_MODIFIED -> _error.value = R.string.not_modified
                UNPROCESSABLE_ENTITY -> _error.value = R.string.unprocessable_entity
                SERVICE_UNAVAILABLE -> _error.value = R.string.service_unavailable
            }
        } else {
            _error.value = R.string.general_error
        }
    }

    companion object {
        const val NOT_MODIFIED = 304
        const val UNPROCESSABLE_ENTITY = 422
        const val SERVICE_UNAVAILABLE = 503
    }
}