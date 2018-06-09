package com.vatril.nightcorer.music

import android.media.MediaPlayer
import java.io.IOException


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

    fun setQueue(queue:List<Song>, position: Int = 0){
        this.position = position
        this.queue = queue
        play()
    }

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

    fun togglePause(){
        if (player.isPlaying)
            player.pause()
        else
            player.start()
    }

    fun next(){
        position++
        play()
    }

    fun previous(){
        if (player.currentPosition > 1000) {
            if(position > 0)
                position--
        }
        play()
    }

}