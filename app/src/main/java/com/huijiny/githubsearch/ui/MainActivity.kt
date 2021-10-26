package com.huijiny.githubsearch.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.huijiny.githubsearch.R
import com.huijiny.githubsearch.base.BindingActivity
import com.huijiny.githubsearch.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity: BindingActivity<ActivityMainBinding> (
    R.layout.activity_main
) {
    private val viewModel by viewModels<MainViewModel>()
    private val adapter = MainAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(this)
        onSetUpViews()
    }

    private fun onSetUpViews() {
        viewModel.repositories
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                adapter.submitList(it)
                Log.d("adpater", it.toString())
            }, {
                Log.d("adpater", it.message.toString())
            })
            .addTo(compositeDisposable)

        val observableTextQuery = Observable.create(
            ObservableOnSubscribe { emitter: ObservableEmitter<String>? ->
                binding.searchBar.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    }

                    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        emitter?.onNext(s.toString())
                    }

                    override fun afterTextChanged(p0: Editable?) {
                    }

                })

            })
            .debounce (500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

        observableTextQuery
            .subscribe(object : Observer<String> {
                override fun onNext(query: String) {
                    viewModel.searchRepository(query)
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }

            })
    }

}