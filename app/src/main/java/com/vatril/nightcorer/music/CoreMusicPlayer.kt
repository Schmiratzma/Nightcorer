package com.vatril.nightcorer.music

import android.media.MediaPlayer
import java.io.IOException

/**
 * The CoreMusicPlayer plays the music
 * Playes the next title after it finished the current
 */
object CoreMusicPlayer {

    private val player = MediaPlayer()
    private var queue:List<Song>? = null
    private var position: Int = 0

    init {
        player.setOnPreparedListener(MediaPlayer::start)
        player.setOnCompletionListener {
            next()
        }
    }

    /**
     * sets a list to be played and starts at the beginning or a specified position
     *
     * @param queue the queue that is going to be played
     * @param position the position where the player start, defaults to 0
     */
    fun setQueue(queue:List<Song>, position: Int = 0){
        this.position = position
        this.queue = queue
        play()
    }

    /**
     * plays the current position from the beginning
     * skips to next on a loading error
     */
    fun play(){
        player.reset()
        if(queue != null)
            if(position < queue!!.size)
                try{
                    player.setDataSource(queue!![position].path)
                    player.prepare()
                }catch (e:IOException){
                    //TODO find a way to show the error
                    next()
                }
    }

    /**
     * pauses playback if the player is playing
     * unpauses if the player is not playing
     */
    fun togglePause(){
        if (player.isPlaying)
            player.pause()
        else
            player.start()
    }

    /**
     * skips to the next title
     */
    fun next(){
        position++
        play()
    }

    /**
     * if the current title is played less then a second it reverts back to the previous
     * if not it just plays the title fom the beginning
     */
    fun previous(){
        if (player.currentPosition > 1000) {
            if(position > 0)
                position--
        }
        play()
    }

}