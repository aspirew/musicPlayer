package rafal.behrendt.musicplayer

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import java.io.IOException
import java.io.InputStream

class MusicPlayerService : Service() {

    val CHANNEL_ID = "musicPlayerServiceChannel"
    private val mediaPlayer : MediaPlayer = MediaPlayer()

    private val mmr = MediaMetadataRetriever()

    private var currentSong = 0
    private val listOfSongs : LinkedHashSet<String> = linkedSetOf()

    val mediaplayer : MediaPlayer = MediaPlayer()
    var song : Song? = null

    override fun onCreate() {
        super.onCreate()
        loadSongs("music")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val song : String? = intent?.getStringExtra("song")

        val notificationIntent = Intent(this, SongPlayer::class.java)
        val pendingIntent : PendingIntent = PendingIntent
            .getActivity(this, 0, notificationIntent, 0)

        val notification : Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Music player")
            .setContentText(song)
            .setSmallIcon(R.drawable.ic_baseline_music_note_24)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun checkIfPlaying(newSong: Song?){
        if (song != null && newSong?.name == song?.name){

        }
        else{
            mediaPlayer.stop()
            mediaPlayer.release()
            setNewSong(newSong?.name)
        }
    }

    private fun setNewSong(songName : String?){

        val am: AssetManager = this.assets
        val afdSong: AssetFileDescriptor = am.openFd("music/${songName}/${songName}.mp3")

        mediaPlayer.setDataSource(afdSong.fileDescriptor, afdSong.startOffset, afdSong.length)
        mediaplayer.prepare()
    }


    private fun loadSongs(path: String): Boolean {
        val list: Array<String>
        try {
            list = this.assets.list(path) as Array<String>
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