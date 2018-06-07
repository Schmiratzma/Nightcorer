package com.vatril.nightcorer.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

data class Song(val title: String, val artist: String, val album: String, val durationInSeconds: Int, val genre: String) {
    private var retriever: MediaMetadataRetriever? = null

    val cover: Bitmap? by lazy {
        if (retriever != null) {
            val embed = retriever!!.embeddedPicture
            val btm = BitmapFactory.decodeByteArray(embed, 0, embed.size)
            Bitmap.createScaledBitmap(btm,250,btm.height/(btm.width/250),false)
        }else
        null
    }

    constructor(title: String, data: MediaMetadataRetriever) : this(title,
            if (data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) != null)
                data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) else "Unknown Artist",
            if (data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) != null)
                data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) else "Unknown Album",
            try {
                Integer.parseInt(data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))/1000
            } catch (e: NumberFormatException) {
                0
            }catch (e: NullPointerException){
                0
            },
            if (data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) != null)
                data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) else "Unknown Genre"
    ) {
        retriever = data
    }
}

fun mockMusic(num: Int) = Array(num, { Song("Title $it", "Artist ${it / 2}", "Album ${it / 3}", it * 20, "Genre ${it % 4}") })

fun timeToStamp(timeInSeconds:Int):String{
    if (timeInSeconds == 0)
        return ""
    val h = timeInSeconds / 3600
    val m = (timeInSeconds - h * 3600) / 60
    val s = timeInSeconds - (h * 3600 + m * 60)

    return "${if (h>0){"$h:"}else{""}}${if(m == 0){0}else{m}}:${if (s<10){"0$s"}else if(s == 0){"00"}else{s}}"
}