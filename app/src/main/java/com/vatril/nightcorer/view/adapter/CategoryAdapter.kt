package com.vatril.nightcorer.view.adapter

import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.vatril.nightcorer.music.Category
import com.vatril.nightcorer.music.MusicManager
import com.vatril.nightcorer.view.MusicView


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
        val music = MusicManager.getMusicList(context!!)

        class MusicAdapter:RecyclerView.Adapter<MusicAdapter.MusicViewHolder>(){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MusicViewHolder(
                    MusicView(context, mode = Category.values()[arguments!!.getInt(posKey)]))

            override fun getItemCount():Int = when(arguments!!.getInt(posKey)){
                    Category.SONGS.ordinal -> music.size
                    Category.ALBUMS.ordinal -> music.distinctBy { it.album }.size
                    Category.ARTISTS.ordinal -> music.distinctBy { it.artist }.size
                    Category.GENRES.ordinal -> music.distinctBy { it.genre }.size
                    Category.PLAYLISTS.ordinal -> 0
                    else ->  0
                }

            override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
                if(holder.itemView is MusicView){
                    holder.itemView.setSong(when(arguments!!.getInt(posKey)){
                        Category.SONGS.ordinal -> music[position]
                        Category.ALBUMS.ordinal -> music.distinctBy { it.album }[position]
                        Category.ARTISTS.ordinal -> music.distinctBy { it.artist }[position]
                        Category.GENRES.ordinal -> music.distinctBy { it.genre }[position]
                        //TODO implement that
                        Category.PLAYLISTS.ordinal -> null
                        else -> null
                })
                }

            }

            internal inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        }

        rv.adapter = MusicAdapter()
        rv.layoutManager = LinearLayoutManager(context)
        return rv
    }
}

class AsyncCategoryAdapterLoader(private val fragmentManager: FragmentManager, private var callback: (CategoryAdapter?) -> Unit) : AsyncTask<Void, Void, CategoryAdapter>(){
    override fun doInBackground(vararg params: Void?): CategoryAdapter = CategoryAdapter(fragmentManager)
    override fun onPostExecute(result: CategoryAdapter?) {
        callback.invoke(result)
    }
}