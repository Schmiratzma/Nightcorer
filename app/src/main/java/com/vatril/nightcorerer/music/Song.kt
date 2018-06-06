package com.vatril.nightcorerer.music

data class Song(val title: String,val artist:String, val album:String, val durationInSeconds:Int,val genre:String)

fun mockMusic(num:Int) = Array(num, {Song("Title $it","Artist ${it/2}","Album ${it/3}",it*20,"Genre ${it%4}")})