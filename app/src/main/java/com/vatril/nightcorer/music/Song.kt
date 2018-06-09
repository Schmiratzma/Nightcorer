package com.vatril.nightcorer.music

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import com.vatril.nightcorer.R

/**
 * The Song class represents a Song
 * it has fields for all important properties
 * use the companions build method for constricting a song
 */
data class Song internal constructor(val title:String, val artist:String, val album:String,
                                     val genre:String, var durationInSeconds:Int,val path:String, val data:MediaMetadataRetriever) {

    /**
     * companion object for building a song
     */
    companion object {
        /**
         * builds a new Song
         * tries to extract all information out if the MediaMetadataRetriever and replaces them with default
         * values if it fails
         *
         * @param title the title of the Song
         * @param data the MediaMetadataRetriever that is used to extract more information
         * @param filename the path so the song can be played
         * @param context the context used to extract string resources
         *
         * @return the constructed song
         */
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

    /**
     * The cover art of the song
     * first call is very slow
     *
     * is null if the song has no cover art
     */
    val cover: Bitmap? by lazy {
        val embed = data!!.embeddedPicture
        val btm = BitmapFactory.decodeByteArray(embed, 0, embed.size)
        Bitmap.createScaledBitmap(btm,250,btm.height/(btm.width/250),false)
    }
}

/**
 * turns a time in seconds into a timestamp
 *
 * @param timeInSeconds the time that is going to be converted
 *
 * @return the string with the timestamp
 */
fun timeToStamp(timeInSeconds:Int):String{
    if (timeInSeconds == 0)
        return ""
    val h = timeInSeconds / 3600
    val m = (timeInSeconds - h * 3600) / 60
    val s = timeInSeconds - (h * 3600 + m * 60)

    return "${if (h>0){"$h:"}else{""}}${if(m == 0){0}else{m}}:${if (s<10){"0$s"}else if(s == 0){"00"}else{s}}"
}