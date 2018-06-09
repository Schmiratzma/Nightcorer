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

/**
 * The key used to transfer the position
 */
private const val posKey = "POS"

/**
 * the CategoryAdapter displays RecyclerViews for the different Categories
 *
 * @param fm the FragmentManager that is used
 */
class CategoryAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){

    /**
     * gets the fragment at the given position
     *
     * @param position the position of the fragment
     *
     * @return the fragment at the position
     */
    override fun getItem(position: Int):Fragment{
        val frag = MusicListFragment()
        frag.arguments = Bundle().apply {
            putInt(posKey,position)
        }
        return frag
    }

    /**
     * returns the number of Categories
     *
     * @return the number of Categories
     */
    override fun getCount(): Int {
        return Category.values().size
    }

    /**
     * gets the current page title
     *
     * @param position the position of the page
     *
     * @return the title of the page at that position
     */
    override fun getPageTitle(position: Int): CharSequence? = Category.values()[position].display
}

/**
 * The MusicListFragment is a fragment that lists MusicViews
 */
class MusicListFragment:Fragment(){

    /**
     * builds the Fragment
     *
     * @param inflater the LayoutInflater unused
     * @param container the container of the Fragment unused
     * @param savedInstanceState unused
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rv = RecyclerView(context)
        val music = MusicManager.getMusicList(context!!)

        /**
         * The MusicAdapter is an RecyclerView Adapter that displays Songs
         */
        class MusicAdapter:RecyclerView.Adapter<MusicAdapter.MusicViewHolder>(){

            /**
             * Creates a MusicViewHolder
             *
             * @param parent the parent of the ViewHolder
             * @param viewType unused
             *
             * @return the MusicViewHolder
             */
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MusicViewHolder(
                    MusicView(context, mode = Category.values()[arguments!!.getInt(posKey)]))

            /**
             * Gives the number of items in the RecyclerView
             *
             * @return the number of items
             */
            override fun getItemCount():Int = when(arguments!!.getInt(posKey)){
                    Category.SONGS.ordinal -> music.size
                    Category.ALBUMS.ordinal -> music.distinctBy { it.album }.size
                    Category.ARTISTS.ordinal -> music.distinctBy { it.artist }.size
                    Category.GENRES.ordinal -> music.distinctBy { it.genre }.size
                    Category.PLAYLISTS.ordinal -> 0
                    else ->  0
                }

            /**
             * Fills the MusicViewHolder with data
             *
             * @param holder the MusicViewHolder to be filled
             * @param position the position of the information
             */
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

/**
 * The AsyncCategoryAdapterLoader constructs the Adapter in the background
 *
 * @param fragmentManager the FragmentManager that is used
 * @param callback the callback that is called when it is done
 *
 */
class AsyncCategoryAdapterLoader(private val fragmentManager: FragmentManager, private var callback: (CategoryAdapter?) -> Unit) : AsyncTask<Void, Void, CategoryAdapter>(){

    /**
     * Constructs the Adapter
     *
     * @param params unused
     */
    override fun doInBackground(vararg params: Void?): CategoryAdapter = CategoryAdapter(fragmentManager)

    /**
     * calls the callback when done
     */
    override fun onPostExecute(result: CategoryAdapter?) {
        callback.invoke(result)
    }
}