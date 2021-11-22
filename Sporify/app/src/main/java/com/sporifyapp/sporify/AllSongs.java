package com.example.sporify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import com.example.sporify.artist.Song.Song;
import com.example.sporify.artist.Song.SongAdapter;

import java.util.ArrayList;
import java.util.List;

public class AllSongs extends AppCompatActivity {


    List<Song> allSongs = new ArrayList<Song>();
    MediaPlayer songPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);
    }

    /*if(ActivityCompat.checkSelfPermissiom(context: this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(activity: this,ArrayOf(Manifest.permission.READ_EXTERNAL_STORAGE). requestCode: 111);
    }
    else
    {
        loadSong()
    }

    @Override
    protected void onRequestPermissionsResult(
            int requestCode,
            List<String> permissions,
            List<Integer> grantResults
    ){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
                if(requestCode==111 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
                    loadSong();
    }


    private void loadSong() {

    }


    public void playsong(String path) {

        try(
        songPlayer.setDataSource(path);
        songPlayer.prepare();
        songPlayer.start();
        )catch (e:Exception){}

    }

    public void stop() {

        songPlayer.stop();

    }
    */
    public int getSize() {
        return allSongs.size();
    }

    public Object getTitle(int position) {

        Song temp = allSongs.get(position);
        return temp.getTitle();

    }

}