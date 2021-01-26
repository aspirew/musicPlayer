package rafal.behrendt.musicplayer

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

class App : Application() {
    val CHANNEL_ID = "musicPlayerServiceChannel"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel : NotificationChannel = NotificationChannel(
            CHANNEL_ID,
            "Example Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )

        val manager : NotificationManager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}