package com.huijiny.githubsearch.base

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo

abstract class BaseViewModel : ViewModel() {
    private val disposables by lazy { CompositeDisposable() }

    fun Disposable.addToDisposable() = addTo(disposables)

    override fun onCleared() {
        disposables.clear()
        super.onCleared()
    }
}