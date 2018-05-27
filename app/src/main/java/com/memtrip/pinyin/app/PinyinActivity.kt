package com.memtrip.pinyin.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout

import android.support.v7.widget.SearchView
import com.memtrip.pinyin.*

import com.memtrip.pinyin.kit.gone
import com.memtrip.pinyin.kit.visible

import kotlinx.android.synthetic.main.pinyin_activity.*
import javax.inject.Inject

class PinyinActivity(override var currentSearchQuery: String = "")
    : PresenterActivity<PinyinView>(), PinyinView {

    @Inject lateinit var presenter: PinyinPresenter

    internal lateinit var fragmentAdapter: PinyinFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pinyin_activity)

        fragmentAdapter = PinyinFragmentAdapter(
                R.id.pinyin_activity_fragment_container,
                pinyin_activity_tablayout,
                supportFragmentManager,
                context())

        pinyin_activity_tablayout.addOnTabSelectedListener(object : OnTabSelectedListenerAdapter() {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                super.onTabSelected(tab)
                sendEvent(TabSelectedEvent(R.id.pinyin_activity_tablayout, tab!!.position))
            }
        })

        pinyin_activity_searchview.setOnQueryTextFocusChangeListener { _ , hasFocus ->
            if (hasFocus) {
                pinyin_activity_searchview_label.gone()
            } else {
                if (pinyin_activity_searchview.query.isEmpty())
                    pinyin_activity_searchview_label.visible()
            }
        }

        pinyin_activity_searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p: String): Boolean { return false }

            override fun onQueryTextChange(terms: String): Boolean {
                currentSearchQuery = terms
                sendSearchEvent(terms)
                return true
            }
        })

        pinyin_activity_searchview.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                sendSearchEvent()
                return false
            }
        })

        pinyin_activity_searchview_label.setOnClickListener({
            pinyin_activity_searchview.isIconified = false
        })
    }

    private fun sendSearchEvent(terms:CharSequence = "") {
        fragmentAdapter.sendEvent(SearchEvent(
                R.id.pinyin_activity_search_terms, terms))
    }

    override fun inject() {
        DaggerPinyinComponent.create().inject(this)
    }

    override fun presenter(): Presenter<PinyinView> = presenter

    override fun view(): PinyinView = this

    override fun updateSearchHint(hint: String) {
        pinyin_activity_searchview.queryHint = hint
        pinyin_activity_searchview_label.text = hint
    }

    companion object {
        fun newIntent(context: Context): Intent =
                Intent(context, PinyinActivity::class.java)
    }
}