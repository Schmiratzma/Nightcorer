package com.vatril.nightcorer.music

/**
 * Category by which music can be sorted
 *
 * @param display the name that is displayed to the user
 *
 * TODO make but names in strings.xml
 */
enum class Category(val display:String) {
    SONGS("Songs"),
    ARTISTS("Artists"),
    ALBUMS("Albums"),
    GENRES("Genres"),
    PLAYLISTS("Playlists")

}