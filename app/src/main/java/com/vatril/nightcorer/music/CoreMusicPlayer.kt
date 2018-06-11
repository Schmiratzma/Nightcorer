package com.vatril.nightcorer.music

import android.media.MediaPlayer
import java.io.IOException

/**
 * The CoreMusicPlayer plays the music
 * Playes the next title after it finished the current
 */
object CoreMusicPlayer {

    private val player = MediaPlayer()
    private var queue:MutableList<Song>? = null
    private var position: Int = 0

    var currentTime:Int
        get() = player.currentPosition/1000
        set(value) = player.seekTo(value*1000)

    var paused:Boolean
        get() = !player.isPlaying
        set(value) =
            if (value)
                player.pause()
            else
                player.start()

    var repeat = false

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
        this.queue = MutableList(queue.size,{queue[it]})
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
        if(!repeat)
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

    fun shuffle(){
        if(queue != null){
            val cur = queue!![position]
            queue!!.shuffle()
            val temp = queue!![0]
            queue!![0] = cur
            queue!! += temp
            position = 0
        }
    }

    fun getCurrentInfo():Song?{
        if(queue != null)
            if(position < queue!!.size)
                return queue!![position]
        return null
    }

    fun getLastInfo(){

    }

    fun getNextInfo(){

    }

}