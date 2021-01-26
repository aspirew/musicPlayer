package rafal.behrendt.musicplayer

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider


class SongPlayer : AppCompatActivity() {

    private lateinit var runnable: Runnable

    private lateinit var viewModel: SongPlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_player)
        viewModel = ViewModelProvider(this).get(SongPlayerViewModel::class.java)
        runnable = viewModel.getRunnableVar()

        if(!viewModel.initiated) {
            val intent = intent
            intent.getStringExtra("song")?.let { viewModel.setName(it) }
        }

        initiatePlayer()
    }

    private fun initiateService(song: String){
        val serviceIntent = Intent(this, MusicPlayerService::class.java)
        serviceIntent.putExtra("song", song)

        startService(serviceIntent)
    }

    private fun reinitiatePlayer(){
        MusicPlayer.songName?.let { viewModel.setName(it) }
        viewModel.initiated = false
        initiatePlayer()
    }

    private fun initiatePlayer(){

        MusicPlayer.changeSong(viewModel.getName())
        initiateService(viewModel.getName())

        viewModel.setName(viewModel.getName()) // głupie ale nie działa bez tego

        viewModel.initiated = true

        val seekbar = findViewById<SeekBar>(R.id.seekbar)
        val playBtn = findViewById<ImageButton>(R.id.playImageButton)
        val skipNextBtn = findViewById<ImageButton>(R.id.skipForwardImageButton)
        val skipBackBtn = findViewById<ImageButton>(R.id.skipBackwardImageButton)
        val forwardBtn = findViewById<ImageButton>(R.id.forwardImageButton)
        val replayBtn = findViewById<ImageButton>(R.id.replayImageButton)
        val listButton = findViewById<ImageButton>(R.id.musicListButton)
        val image = findViewById<ImageView>(R.id.songImage)
        val name = findViewById<TextView>(R.id.songNameTV)

        image.setImageDrawable(Drawable.createFromStream(viewModel.image, null))
        name.text = viewModel.getName()

        seekbar.max = MusicPlayer.mediaplayer.duration

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    MusicPlayer.mediaplayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        runnable = Runnable {
            seekbar.progress = MusicPlayer.mediaplayer.currentPosition
            viewModel.getHandler().postDelayed(runnable, 1000)
        }
        viewModel.getHandler().postDelayed(runnable, 1000)

        MusicPlayer.mediaplayer.setOnCompletionListener {
            MusicPlayer.nextSong()
            MusicPlayer.songName?.let { viewModel.setName(it) }
            viewModel.initiated = false
            initiatePlayer()
            initiateService(viewModel.getName())
        }


        if(MusicPlayer.mediaplayer.isPlaying)
            playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
        else
            playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)

        playBtn.setOnClickListener {
            if(!MusicPlayer.mediaplayer.isPlaying){
                MusicPlayer.play()
                playBtn.setImageResource(R.drawable.ic_baseline_pause_24)
            }
            else{
                MusicPlayer.pause()
                playBtn.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }
        }

        skipNextBtn.setOnClickListener {
            MusicPlayer.nextSong()
            reinitiatePlayer()
        }

        skipBackBtn.setOnClickListener {
            MusicPlayer.previousSong()
            reinitiatePlayer()
        }

        forwardBtn.setOnClickListener {
            MusicPlayer.forward()
        }

        replayBtn.setOnClickListener {
            MusicPlayer.backward()
        }

        listButton.setOnClickListener {
            val myIntent = Intent(this, MainActivity::class.java)
            ContextCompat.startActivity(this, myIntent, null)
        }

    }
}