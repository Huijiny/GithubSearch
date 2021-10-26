package com.huijiny.githubsearch.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
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
import retrofit2.HttpException
import java.util.concurrent.TimeUnit

class MainActivity : BindingActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val viewModel by viewModels<MainViewModel>()
    private val mainAdapter = MainAdapter()
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initRecycler()
        onSetUpViews()
        setTextWatcher()
    }

    private fun initRecycler() {
        binding.recycler.run {
            adapter = mainAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun onSetUpViews() {
        viewModel.error
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it is HttpException) {
                    when (it.code()) {
                        304 -> toastError(getString(R.string.not_modified))
                        422 -> toastError(getString(R.string.unprocessable_entity))
                        503 -> toastError(getString(R.string.service_unavailable))
                    }
                } else {
                    toastError(getString(R.string.general_error))
                }
            }
            .addTo(compositeDisposable)

        viewModel.repositories
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                mainAdapter.submitList(it)
            }
            .addTo(compositeDisposable)
    }

    private fun setTextWatcher() {
        val observableTextQuery = Observable.create(
            ObservableOnSubscribe { emitter: ObservableEmitter<String>? ->
                binding.searchBar.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                    override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                        emitter?.onNext(s.toString())
                    }

                    override fun afterTextChanged(p0: Editable?) {}
                })
            })
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())

        observableTextQuery
            .subscribe(object : Observer<String> {
                override fun onNext(query: String) {
                    viewModel.searchRepository(query)
                }

                override fun onSubscribe(d: Disposable) {}

                override fun onError(e: Throwable) {}

                override fun onComplete() {}
            })
    }

    private fun toastError(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

}