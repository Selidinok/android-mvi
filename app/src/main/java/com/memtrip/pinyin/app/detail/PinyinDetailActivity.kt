package com.memtrip.pinyin.app.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.memtrip.pinyin.Presenter
import com.memtrip.pinyin.PresenterActivity
import com.memtrip.pinyin.R
import com.memtrip.pinyin.api.PinyinEntity
import kotlinx.android.synthetic.main.pinyin_detail_activity.*
import javax.inject.Inject

class PinyinDetailActivity : PresenterActivity<PinyinDetailView>(), PinyinDetailView {

    @Inject lateinit var presenter: PinyinDetailPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pinyin_detail_activity)

        setSupportActionBar(pinyin_detail_activity_toolbar)

        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_home_up)
    }

    override fun inject() {
        DaggerPinyinDetailComponent
                .builder()
                .pinyinEntity(PinyinParcel.out(intent))
                .build()
                .inject(this)
    }

    override fun presenter(): Presenter<PinyinDetailView> = presenter

    override fun view(): PinyinDetailView = this

    override fun populate(pinyinParcel: PinyinParcel) {
        supportActionBar!!.setTitle(pinyinParcel.phoneticScriptText)
        pinyin_detail_activity_phonetic_script_value.text = pinyinParcel.phoneticScriptText
        pinyin_detail_activity_english_translation_value.text = pinyinParcel.englishTranslationText
    }

    companion object {
        fun newIntent(context: Context, pinyinEntity: PinyinEntity): Intent {
            val intent = Intent(context, PinyinDetailActivity::class.java)
            PinyinParcel.into(pinyinEntity, intent)
            return intent
        }
    }
}