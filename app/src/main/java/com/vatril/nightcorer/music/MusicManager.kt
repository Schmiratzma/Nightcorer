package com.vatril.nightcorer.music

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.vatril.nightcorer.R

/**
 * The MusicManager loads the music
 */
object MusicManager{

    private var musicList:List<Song>? = null

    /**
     * loads the music from the phones storage
     *
     * @return a list of all music
     */
    fun getMusicList(context:Context):List<Song>{

        if (musicList != null){
            return musicList!!
        }

        val cr = context.contentResolver
        val cur = cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media.DATA, MediaStore.MediaColumns.TITLE),
                "${MediaStore.Audio.Media.IS_MUSIC} != 0",null,"${MediaStore.Audio.Media.TITLE} ASC")
        val songs = mutableListOf<Song>()

        if(cur.count > 0){
            while (cur.moveToNext()){
                try {
                    val mmr = MediaMetadataRetriever()
                    mmr.setDataSource(context, Uri.parse(Uri.encode(cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)))))
                    songs.add(Song.build(cur.getString(cur.getColumnIndex(MediaStore.MediaColumns.TITLE)),
                            mmr,cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA)),context))

                }catch (exc:RuntimeException){
                    Log.e(context.getString(R.string.app_name),"Error loading: ${cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.TITLE))}")
                }
            }
        }
        cur.close()
        musicList = songs
        return musicList!!
    }
}