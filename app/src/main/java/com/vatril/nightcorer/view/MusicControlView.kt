package com.vatril.nightcorer.view

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.vatril.nightcorer.R
import com.vatril.nightcorer.music.CoreMusicPlayer
import com.vatril.nightcorer.music.Song

class MusicControlView : FrameLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val sceneCollapsed = Scene.getSceneForLayout(this, R.layout.bottom_panel_collapsed, context)
    private val sceneExpanded = Scene.getSceneForLayout(this, R.layout.bottom_panel_expanded, context)
    private val transition = TransitionInflater.from(context).inflateTransition(R.transition.sliding_up_panel_transition)


    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        inflate(context, R.layout.bottom_panel_collapsed, this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setUpView()
        val bottomSheet = BottomSheetBehavior.from(this)

        bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            var last = BottomSheetBehavior.STATE_COLLAPSED

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (slideOffset == 1.0f) {
                    TransitionManager.go(sceneExpanded, transition)
                    last = BottomSheetBehavior.STATE_EXPANDED
                    setUpView()
                } else if (slideOffset == 0f) {
                    TransitionManager.go(sceneCollapsed, transition)
                    last = BottomSheetBehavior.STATE_COLLAPSED
                    setUpView()
                }else if(last == BottomSheetBehavior.STATE_EXPANDED && slideOffset < 0.9f){
                    TransitionManager.go(sceneCollapsed, transition)
                    last = BottomSheetBehavior.STATE_COLLAPSED
                    setUpView()
                }
            }

            override fun onStateChanged(bottom: View, newState: Int) {
            }

        })
    }

    fun setUpView() {
        val prev = findViewById<ImageButton>(R.id.prev_button)
        val playPause = findViewById<ImageButton>(R.id.play_pause_button)
        val next = findViewById<ImageButton>(R.id.next_button)

        val shuffle = findViewById<ImageButton>(R.id.shuffle_button)
        val repeat = findViewById<ImageButton>(R.id.loop_button)

        val image = findViewById<ImageView>(R.id.album_cover)

        val songTitle = findViewById<TextView>(R.id.songname_textfield)
        val artistName = findViewById<TextView>(R.id.author_textfield)
        val albumName = findViewById<TextView>(R.id.album_textfield)

        val musicLength = findViewById<TextView>(R.id.music_length)
        val musicTime = findViewById<TextView>(R.id.music_time)

        val seeker = findViewById<SeekBar>(R.id.music_seek_bar)

        prev.setOnClickListener {
            CoreMusicPlayer.previous()
        }

        next.setOnClickListener {
            CoreMusicPlayer.next()
        }

        playPause.setOnClickListener {
            CoreMusicPlayer.togglePause()
        }

        shuffle.setOnClickListener{
            CoreMusicPlayer.shuffle()
        }

        repeat.setOnClickListener {
            CoreMusicPlayer.repeat = !CoreMusicPlayer.repeat
        }

        var song: Song? = null

        class Updater : Runnable {
            override fun run() {

                if (CoreMusicPlayer.getCurrentInfo() == null) {

                } else {
                    if (song != CoreMusicPlayer.getCurrentInfo()) {
                        song = CoreMusicPlayer.getCurrentInfo()
                        if (song != null) {
                            musicLength.text = com.vatril.nightcorer.music.timeToStamp(song!!.durationInSeconds)
                            songTitle.text = song!!.title
                            artistName.text = song!!.artist
                            albumName.text = song!!.album
                            image.setImageBitmap(song!!.cover)
                            seeker.max = song!!.durationInSeconds
                        }
                    }
                    if (CoreMusicPlayer.paused)
                        playPause.setImageResource(R.drawable.button_play)
                    else
                        playPause.setImageResource(R.drawable.button_pause)
                    if (CoreMusicPlayer.repeat)
                        repeat.setImageResource(R.drawable.button_repeat_active)
                    else
                        repeat.setImageResource(R.drawable.button_repeat)
                    musicTime.text = com.vatril.nightcorer.music.timeToStamp(CoreMusicPlayer.currentTime)
                    seeker.progress = CoreMusicPlayer.currentTime
                    seeker.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
                        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                            musicTime.text = com.vatril.nightcorer.music.timeToStamp(seekBar!!.progress)
                        }

                        override fun onStartTrackingTouch(seekBar: SeekBar?) {

                        }

                        override fun onStopTrackingTouch(seekBar: SeekBar?) {
                            CoreMusicPlayer.currentTime = seekBar!!.progress
                        }

                    })
                }
                if (handler != null)
                    handler.postDelayed(this, 250)
            }

        }
        handler.post(Updater())

    }
}