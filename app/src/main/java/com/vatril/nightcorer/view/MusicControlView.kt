package com.vatril.nightcorer.view

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.vatril.nightcorer.R

class MusicControlView:FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val sceneCollaped = Scene.getSceneForLayout(this,R.layout.bottom_panel_collapsed,context)
    val sceneExpanded = Scene.getSceneForLayout(this,R.layout.bottom_panel_expanded,context)
    val transition = TransitionInflater.from(context).inflateTransition(R.transition.sliding_up_panel_transition)

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        inflate(context, R.layout.bottom_panel_collapsed, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setDisplay()
        val bottomSheet = BottomSheetBehavior.from(this)

        bottomSheet.setBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
            var last = BottomSheetBehavior.STATE_COLLAPSED

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if(slideOffset == 1.0f){
                    TransitionManager.go(sceneExpanded,transition)
                    last = BottomSheetBehavior.STATE_EXPANDED
                    setDisplay()
                }else if(slideOffset == 0f){
                    TransitionManager.go(sceneCollaped,transition)
                    last = BottomSheetBehavior.STATE_COLLAPSED
                    setDisplay()
                }
            }

            override fun onStateChanged(bottom: View, newState: Int) {
                if(last == BottomSheetBehavior.STATE_EXPANDED && newState == BottomSheetBehavior.STATE_DRAGGING){
                    TransitionManager.go(sceneCollaped,transition)
                    last = BottomSheetBehavior.STATE_COLLAPSED
                    setDisplay()
                }
            }

        })
    }

    fun setDisplay(){
        findViewById<TextView>(R.id.author_textfield).text = "Artist"
        findViewById<TextView>(R.id.album_textfield).text = "Album"
        findViewById<TextView>(R.id.songname_textfield).text = "Song Title"

        findViewById<TextView>(R.id.music_time).text = com.vatril.nightcorer.music.timeToStamp(123)
        findViewById<TextView>(R.id.music_length).text = com.vatril.nightcorer.music.timeToStamp(165)
    }
}