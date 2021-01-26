package rafal.behrendt.musicplayer

import android.app.Application
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.os.Parcelable
import androidx.lifecycle.AndroidViewModel
import java.io.IOException
import kotlinx.android.parcel.Parcelize

class MainActivityViewModel(private val app: Application) : AndroidViewModel(app) {

    private val listOfSongs: MutableList<Song> = mutableListOf()

    init{
        loadSongs("music")
    }

    private fun loadSongs(path: String): Boolean {
        val list: Array<String>
        try {
            list = app.assets.list(path) as Array<String>
            if (list.isNotEmpty()) {
                for (file in list) {
                    listOfSongs.add(parseSong(file))
                }
            }
        } catch (e: IOException) {
            return false
        }
        return true
    }

    fun getSongs(): List<Song> {
        return listOfSongs.toList()
    }

    private fun parseSong(song : String) : Song{
        val am: AssetManager = app.assets
        val afdSong: AssetFileDescriptor = am.openFd("music/${song}/${song}.mp3")

        val mmr = MediaMetadataRetriever()

        mmr.setDataSource(afdSong.fileDescriptor, afdSong.startOffset, afdSong.length)

        val newSong = Song(song, mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR).toString(),
                mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM).toString())

        afdSong.close()

        return newSong

    }
}

@Parcelize
data class Song(val name: String, val author: String, val album: String) : Parcelable