package com.consistence.pinyin

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import io.reactivex.disposables.CompositeDisposable

abstract class ViewActivity<I : ViewIntent, S : ViewState, L : ViewLayout> : AppCompatActivity() {

    private val d = CompositeDisposable()
    private var init = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
        d.add(model().states().subscribe({render().state(layout(), it)}))
    }

    override fun onStart() {
        super.onStart()

        if (!init) {
            init = true
            initIntent()?.let { model().publish(it) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    protected inline fun <reified T : ViewModel> getViewModel(viewModelFactory: ViewModelProvider.Factory): T =
        ViewModelProviders.of(this, viewModelFactory)[T::class.java]

    abstract fun inject()

    abstract fun layout(): L

    abstract fun model(): Model<I, S>

    abstract fun render() : ViewRender<L, S>

    open fun initIntent() : I? = ViewIntent.NONE
}