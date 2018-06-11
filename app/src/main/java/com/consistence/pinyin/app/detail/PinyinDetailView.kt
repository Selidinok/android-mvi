package com.consistence.pinyin.app.detail

import com.consistence.pinyin.ViewIntent
import com.consistence.pinyin.ViewLayout
import com.consistence.pinyin.ViewRender
import com.consistence.pinyin.ViewState
import dagger.Module
import dagger.android.ContributesAndroidInjector

sealed class PinyinDetailIntent : ViewIntent {
    object Init: PinyinDetailIntent()
    object PlayAudio: PinyinDetailIntent()
}

sealed class PinyinDetailState : ViewState {
    data class Populate(val phoneticScriptText: String,
                        val englishTranslationText: String,
                        val chineseCharacters: String,
                        val audioSrc: String?) : PinyinDetailState()
    data class PlayAudio(val audioSrc: String) : PinyinDetailState()
}

interface PinyinDetailLayout : ViewLayout {
    fun populate(phoneticScriptText: String,
                 englishTranslationText: String,
                 chineseCharacters: String)
    fun showAudioControl()
    fun playAudio(audioSrc: String)
}

class PinyinDetailRender(private val layout: PinyinDetailLayout) : ViewRender<PinyinDetailState> {
    override fun state(state: PinyinDetailState) = when(state) {
        is PinyinDetailState.Populate -> {
            state.audioSrc?.let {
                layout.showAudioControl()
            }
            layout.populate(
                    state.phoneticScriptText,
                    state.englishTranslationText,
                    state.chineseCharacters)
        }
        is PinyinDetailState.PlayAudio -> {
            layout.playAudio(state.audioSrc)
        }
    }
}

@Module
abstract class PinyinDetailActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributesPinyinDetailActivity() : PinyinDetailActivity
}