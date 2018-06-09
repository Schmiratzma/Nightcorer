package com.vatril.nightcorer.view

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

import com.vatril.nightcorer.R
import com.vatril.nightcorer.music.Category
import com.vatril.nightcorer.music.Song

/**
 * The MusicView can display a song or one of its defining properties
 *
 * @param context the context the View is in
 * @param mode the category the View should display
 */
class MusicView(context: Context?, val mode: Category = Category.SONGS) : FrameLayout(context) {
    /**
     * secondary constructor that just needs a context and defaults to the Song category
     *
     * @param context the context the View is in
     */
    constructor(context: Context?) : this(context, Category.SONGS)

    private var bitmapThread: Thread? = null
    private var cover: Bitmap? = null
    private var coverDone = false

    init {
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (mode == Category.SONGS)
            inflate(context, R.layout.music_display, this)
        else
            inflate(context, R.layout.other_category_music_display, this)
    }

    /**
     * sets the song that is suppose to be displayed
     *
     * @param song the song that is displayed
     */
    fun setSong(song: Song?) {
        if (song == null) {
            if (mode != Category.SONGS) {
                clear()
            } else {
                findViewById<TextView>(R.id.card_textfield).text = ""
            }
            return
        }
        if (mode != Category.SONGS) {
            findViewById<TextView>(R.id.card_textfield).text = when (mode) {

                Category.ARTISTS -> song.artist
                Category.ALBUMS -> song.album
                Category.GENRES -> song.genre
            //TODO implement that
                Category.PLAYLISTS -> "Not Implemented"
                else -> "ERROR"
            }
            return
        }
        clear()
        findViewById<TextView>(R.id.card_title).text = song.title
        findViewById<TextView>(R.id.card_artist).text = song.artist
        findViewById<TextView>(R.id.card_duration).text = com.vatril.nightcorer.music.timeToStamp(song.durationInSeconds)
        bitmapThread = Thread({
            cover = song.cover
            coverDone = true
        }, "Bitmap getting Thread")
        bitmapThread!!.start()
        val handler = Handler(Looper.getMainLooper())

        /**
         * displays the cover after it has loaded
         */
        class Run : Runnable {
            override fun run() {
                if (coverDone) {
                    if (cover != null) {
                        val coverView = findViewById<ImageView>(R.id.card_cover_image)
                        coverView.setImageBitmap(cover)
                        coverView.alpha = 0f
                        coverView.visibility = View.VISIBLE
                        coverView.animate().alpha(1f).duration = 300

                    }
                } else {
                    handler.post(this)
                }
            }
        }
        handler.post(Run())
    }


    /**
     * clears the display
     */
    private fun clear() {
        coverDone = false
        findViewById<TextView>(R.id.card_title).text = ""
        findViewById<TextView>(R.id.card_artist).text = ""
        findViewById<TextView>(R.id.card_duration).text = ""
        findViewById<ImageView>(R.id.card_cover_image).visibility = View.GONE
        if (bitmapThread != null)
            bitmapThread!!.interrupt()
        cover = null
    }
}