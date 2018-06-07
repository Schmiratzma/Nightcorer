package com.vatril.nightcorer.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

data class Song(val title: String, val artist: String, val album: String, val durationInSeconds: Int, val genre: String) {
    private var retriever: MediaMetadataRetriever? = null

    val cover: Bitmap? by lazy {
        if (retriever != null) {
            val embed = retriever!!.embeddedPicture
            BitmapFactory.decodeByteArray(embed, 0, embed.size)
        }
        null
    }

    constructor(title: String, data: MediaMetadataRetriever) : this(title,
            data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST),
            data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM),
            try {
                Integer.parseInt(data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
            } catch (e: NumberFormatException) {
                0
            },
            data.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE)
    ) {
        retriever = data
    }
}

fun mockMusic(num: Int) = Array(num, { Song("Title $it", "Artist ${it / 2}", "Album ${it / 3}", it * 20, "Genre ${it % 4}") })