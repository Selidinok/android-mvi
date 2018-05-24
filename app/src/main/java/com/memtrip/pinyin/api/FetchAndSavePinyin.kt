package com.memtrip.pinyin.api

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import javax.inject.Inject

class FetchAndSavePinyin @Inject internal constructor(
        private val fetchPinyin: FetchPinyin,
        private val savePinyin: SavePinyin) {

    fun save(success: Action, error: Consumer<Throwable>) : Disposable {

        val d = CompositeDisposable()

        d.add(fetchPinyin.values(Consumer {
            d.add(savePinyin.insert(it.pinyin, success, error))
        }, error))

        return d
    }
}