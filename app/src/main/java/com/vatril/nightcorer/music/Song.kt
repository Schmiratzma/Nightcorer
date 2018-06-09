package com.vatril.nightcorer.music

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.vatril.nightcorer.R

data class Song internal constructor(val title:String, val artist:String, val album:String,
                                     val genre:String, var durationInSeconds:Int,val path:String, val data:MediaMetadataRetriever) {

    companion object {
        fun build(title: String,data: MediaMetadataRetriever,filename:String, context:Context):Song{
            val artist = (if (data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) != null)
                data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) else context.getString(R.string.unknown_artist))!!

            val album = (if (data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) != null)
                data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM) else context.getString(R.string.unknown_album))!!

            val genre = (if (data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) != null)
                data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE) else context.getString(R.string.unknown_genre))!!

            val durationInSeconds = try {
                Integer.parseInt(data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))/1000
            } catch (e: NumberFormatException) {
                0
            }catch (e: NullPointerException){
                0
            }

            return Song(title,artist,album,genre,durationInSeconds,filename, data)
        }
    }


    val cover: Bitmap? by lazy {
        val embed = data!!.embeddedPicture
        val btm = BitmapFactory.decodeByteArray(embed, 0, embed.size)
        Bitmap.createScaledBitmap(btm,250,btm.height/(btm.width/250),false)
    }
}

fun timeToStamp(timeInSeconds:Int):String{
    if (timeInSeconds == 0)
        return ""
    val h = timeInSeconds / 3600
    val m = (timeInSeconds - h * 3600) / 60
    val s = timeInSeconds - (h * 3600 + m * 60)

    return "${if (h>0){"$h:"}else{""}}${if(m == 0){0}else{m}}:${if (s<10){"0$s"}else if(s == 0){"00"}else{s}}"
}