package rafal.behrendt.musicplayer

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

class SongsRecyclerViewAdapter(
    val musicList: List<Song>,
    val context: Context
) : RecyclerView.Adapter<SongsRecyclerViewAdapter.itemViewHolder>() {

    class itemViewHolder(itemView: View,  var song: Song? = null) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val songImage = itemView.findViewById<ImageView>(R.id.songListImage)
        val name = itemView.findViewById<TextView>(R.id.songItemNameTV)
        val author = itemView.findViewById<TextView>(R.id.authorTV)
        val album = itemView.findViewById<TextView>(R.id.albumTV)

        init {
            itemView.setOnClickListener {

                val myIntent = Intent(it.context, SongPlayer::class.java)
                myIntent.putExtra("song", song?.name)
                startActivity(it.context, myIntent, null)
            }
        }

        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): itemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return itemViewHolder(view)
    }

    override fun onBindViewHolder(holder: itemViewHolder, position: Int) {

        val songName = musicList[position].name
        val image = context.assets.open("music/${songName}/cover.jpg")

        holder.songImage.setImageDrawable(Drawable.createFromStream(image, null))
        holder.name.text = musicList[position].name
        holder.author.text = musicList[position].author
        holder.album.text = musicList[position].album
        holder.song = musicList[position]
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

}