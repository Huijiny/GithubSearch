package com.huijiny.githubsearch.ui

import android.util.Log
import com.huijiny.githubsearch.base.BaseViewModel
import com.huijiny.githubsearch.data.model.Repository
import com.huijiny.githubsearch.network.GithubInjection
import com.huijiny.githubsearch.repository.SearchRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class MainViewModel: BaseViewModel() {
    private val repository by lazy {
        SearchRepository(GithubInjection.provideSearchService())
    }

    private val _repositories: BehaviorSubject<List<Repository>> =
        BehaviorSubject.create()
    val repositories: Observable<List<Repository>> = _repositories

    fun searchRepository(query: String) {
        repository.searchRepository(query)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _repositories.onNext(it.repositories)
            }, {
                Log.d("error", it.message.toString())
            })
            .addToDisposable()
    }
}