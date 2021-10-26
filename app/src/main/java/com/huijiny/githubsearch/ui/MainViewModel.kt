package com.huijiny.githubsearch.ui

import com.huijiny.githubsearch.R
import com.huijiny.githubsearch.base.BaseViewModel
import com.huijiny.githubsearch.data.model.Repository
import com.huijiny.githubsearch.network.GithubInjection
import com.huijiny.githubsearch.repository.SearchRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class MainViewModel : BaseViewModel() {
    private val repository by lazy {
        SearchRepository(GithubInjection.provideSearchService())
    }

    private val _repositories: BehaviorSubject<List<Repository>> =
        BehaviorSubject.create()
    val repositories: Observable<List<Repository>> = _repositories

    private val _error: PublishSubject<Int> =
        PublishSubject.create()
    val error: Observable<Int> = _error

    fun searchRepository(query: String) {
        repository.searchRepository(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _repositories.onNext(it.repositories)
            }, {
                getErrorString(it)
            })
            .addToDisposable()
    }

    private fun getErrorString(throwable: Throwable) {
        if (throwable is HttpException) {
            when (throwable.code()) {
                NOT_MODIFIED -> _error.onNext(R.string.not_modified)
                UNPROCESSABLE_ENTITY -> _error.onNext(R.string.unprocessable_entity)
                SERVICE_UNAVAILABLE -> _error.onNext(R.string.service_unavailable)
            }
        } else {
            _error.onNext(R.string.general_error)
        }
    }

    companion object {
        const val NOT_MODIFIED = 304
        const val UNPROCESSABLE_ENTITY = 422
        const val SERVICE_UNAVAILABLE = 503
    }
}