package rafal.behrendt.musicplayer

import android.app.Application
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import java.io.IOException
import java.io.InputStream


class SongPlayerViewModel(private val app: Application) : AndroidViewModel(app) {

    private var runnable: Runnable = Runnable {  }
    private val handler = Handler(Looper.getMainLooper())

    var image: InputStream? = null
    private var nameOfSong: String = "unknown"

    var initiated = false

    fun initiate() {
        initiated = true
    }

    fun getRunnableVar(): Runnable {
        return runnable
    }

    fun setRunnable(runnable: Runnable) {
        this.runnable = runnable
    }

    fun getHandler(): Handler {
        return handler
    }

    fun getName(): String {
        return nameOfSong
    }

    fun setName(name : String) {
        nameOfSong = name
        image = app.assets.open("music/${nameOfSong}/cover.jpg")
    }



}