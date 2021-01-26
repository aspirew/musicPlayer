package rafal.behrendt.musicplayer

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        MusicPlayer.mContext = applicationContext
        MusicPlayer.loadSongs("music")
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        initRecyclerView()
    }

    private fun initRecyclerView(){

        val recyclerView = findViewById<RecyclerView>(R.id.songsRV)
        val adapter = SongsRecyclerViewAdapter(viewModel.getSongs(), applicationContext)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(applicationContext, 1)

    }
}