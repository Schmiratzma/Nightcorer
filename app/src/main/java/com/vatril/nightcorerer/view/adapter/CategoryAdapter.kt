package com.vatril.nightcorerer.view.adapter

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.vatril.nightcorerer.music.Category

private const val posKey = "POS"

class CategoryAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){

    override fun getItem(position: Int):Fragment{
        val frag = MusicListFragment()
        frag.arguments = Bundle().apply {
            putInt(posKey,position)
        }
        return frag
    }

    override fun getCount(): Int {
        return Category.values().size
    }

    override fun getPageTitle(position: Int): CharSequence? = Category.values()[position].display
}

class MusicListFragment:Fragment(){
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rv = RecyclerView(context)

        //Todo demock

        val music = com.vatril.nightcorerer.music.mockMusic(100)

        class MusicAdapter:RecyclerView.Adapter<MusicAdapter.MusicViewHolder>(){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MusicViewHolder(TextView(context))

            override fun getItemCount():Int = when(arguments!!.getInt(posKey)){
                    Category.SONGS.ordinal -> music.size
                    Category.ALBUMS.ordinal -> music.distinctBy { it.album }.size
                    Category.ARTISTS.ordinal -> music.distinctBy { it.artist }.size
                    Category.GENRES.ordinal -> music.distinctBy { it.genre }.size
                    Category.PLAYLISTS.ordinal -> 0
                    else ->  0
                }



            override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
                if(holder.itemView is TextView)
                    holder.itemView.text = when(arguments!!.getInt(posKey)){
                        Category.SONGS.ordinal -> music[position].title
                        Category.ALBUMS.ordinal -> music.distinctBy { it.album }[position].album
                        Category.ARTISTS.ordinal -> music.distinctBy { it.artist }[position].artist
                        Category.GENRES.ordinal -> music.distinctBy { it.genre }[position].genre
                        Category.PLAYLISTS.ordinal -> "Not implemented"
                        else ->  "ERROR"
                    }
            }

            internal inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        }

        rv.adapter = MusicAdapter()
        rv.layoutManager = LinearLayoutManager(context)
        return rv
    }
}