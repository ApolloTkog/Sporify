package com.example.sporify.artist.Song;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.sporify.AllSongs;
import com.example.sporify.R;
import com.example.sporify.artist.Artsist;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends BaseAdapter {

     //extends AllSongs

    AllSongs allSongs;
    List<Song> songList = new ArrayList<Song>();

    public SongAdapter(AllSongs allSongs, List<String> artistBio) {
        allSongs = allSongs;
        artistBio = artistBio;
    }

    @Override
    public int getCount() {
        return allSongs.getSize();
    }

    @Override
    public Object getItem(int position) {
        return allSongs.getTitle(position);
    }

    @Override
    public long getItemId(int position) {
        return Long.valueOf(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View playall = null;
       // playall = LayoutInflater.inflate(R.layout.allsongslayout,null);

        //Eida apo kotlin. Den brhskw akoma to antistixo se java. 8a to ftiaksw se ligo.

        Song song = songList.get(position);
        //Artsist bio = artistBio.get(position);
       /* playall.textView1.text = song.getTitle();
        //playall.textView2.text = bio.getBio();
        playall.button.setOnclickListener{
            if(playall.button.text == "||"){
                allSongs.stop();
                playall.button.text = "►";
            }
            else{
                allSongs.playsong(song.getPath());

                playall.button.text = "||";
            }
        }
       */
        return playall;
    }

}
