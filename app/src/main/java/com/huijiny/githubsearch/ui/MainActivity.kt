package com.huijiny.githubsearch.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import com.huijiny.githubsearch.R
import com.huijiny.githubsearch.base.BindingActivity
import com.huijiny.githubsearch.databinding.ActivityMainBinding
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MainActivity : BindingActivity<ActivityMainBinding>(
    R.layout.activity_main
) {
    private val viewModel by viewModels<MainViewModel>()
    private val mainAdapter = MainAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        onSetUpViews()
    }

    private fun initView() {
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        binding.recycler.adapter = mainAdapter

        setTextWatcher()
    }

    private fun onSetUpViews() {
        viewModel.error.observe(this) {
            if (it != null) {
                showToast(getString(it))
            }
        }
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

    private fun showToast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onDestroy() {
        super.onDestroy()
    }

}