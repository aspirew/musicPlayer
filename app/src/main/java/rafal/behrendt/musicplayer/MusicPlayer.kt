package rafal.behrendt.musicplayer

import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaPlayer
import java.io.IOException

object MusicPlayer {

    val mediaplayer : MediaPlayer = MediaPlayer()
    var currentSong = 0
    val listOfSongs : LinkedHashSet<String> = linkedSetOf()
    var songName : String? = null

    var mContext: Context? = null


    fun changeSong(newSong : String){
        if(songName != newSong) {

            songName = newSong
            currentSong = listOfSongs.indexOf(songName)

            mediaplayer.stop()
            mediaplayer.reset()
            val am: AssetManager? = mContext?.assets
            val afdSong: AssetFileDescriptor? = am?.openFd("music/${newSong}/${newSong}.mp3")

            mediaplayer.setDataSource(afdSong?.fileDescriptor, afdSong?.startOffset!!, afdSong.length)
            mediaplayer.prepare()
            mediaplayer.start()
        }
    }

    fun nextSong(){
        if(++currentSong >= listOfSongs.size) currentSong = 0
        changeSong(listOfSongs.elementAt(currentSong))
    }

    fun previousSong(){
        if(--currentSong < 0) currentSong = listOfSongs.size - 1
        changeSong(listOfSongs.elementAt(currentSong))
    }

    fun forward() {
        val newTime = mediaplayer.currentPosition + 10000
         mediaplayer.seekTo(newTime)
    }

    fun backward(){
        var newTime = mediaplayer.currentPosition - 10000
        if(newTime < 0) newTime = 0
        mediaplayer.seekTo(newTime)
    }

    fun play(){
        mediaplayer.start()
    }

    fun pause(){
        mediaplayer.pause()
    }


    fun loadSongs(path: String): Boolean {

        val list: Array<String>
        try {
            list = mContext?.assets?.list(path) as Array<String>
            if (list.isNotEmpty()) {
                for (file in list) {
                    listOfSongs.add(file)
                }
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }


}