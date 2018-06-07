package com.consistence.pinyin.app.list.character

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife

import com.consistence.pinyin.R
import com.consistence.pinyin.api.PinyinEntity
import com.consistence.pinyin.app.detail.PinyinDetailActivity
import com.consistence.pinyin.app.list.PinyinListFragment
import com.consistence.pinyin.app.list.PinyinListIntent

import com.consistence.pinyin.kit.Interaction
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class PinyinCharacterFragment : PinyinListFragment() {

    @Inject lateinit var model: PinyinCharacterModel

    @BindView(R.id.pinyin_character_fragment_recyclerview)
    lateinit var recyclerView: RecyclerView

    private lateinit var adapter: PinyinCharacterAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.pinyin_character_fragment, container, false)
        ButterKnife.bind(this, view)

        val adapterInteraction: PublishSubject<Interaction<PinyinEntity>> = PublishSubject.create()
        adapter = PinyinCharacterAdapter(context!!, adapterInteraction)
        recyclerView.adapter = adapter

        adapterInteraction.map({
            when (it.id) {
                R.id.pinyin_list_audio_button ->
                    PinyinListIntent.PlayAudio(it.data.audioSrc!!)
                else ->
                    PinyinListIntent.SelectItem(it.data)
            }
        }).subscribe(model.intents)

        return view
    }

    override fun inject() {
        DaggerPinyinCharacterComponent
                .builder()
                .application(activity!!.application)
                .build()
                .inject(this)
    }

    override fun model() = model

    override fun populate(pinyin: List<PinyinEntity>) {
        adapter.clear()
        adapter.populate(pinyin)
    }

    override fun navigateToPinyinDetails(pinyinEntity: PinyinEntity) {
        startActivity(PinyinDetailActivity.newIntent(context!!, pinyinEntity))
    }

    override fun error() {

    }

    companion object { fun newInstance()  = PinyinCharacterFragment() }
}