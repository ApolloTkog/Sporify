package com.myapp.sporify.interfaces;

import androidx.lifecycle.LiveData;

import com.myapp.sporify.models.Album;
import com.myapp.sporify.models.Artist;
import com.myapp.sporify.models.Track;

import java.util.List;

public interface ApiService {
    LiveData<List<Artist>> getTopArtists();
    LiveData<List<Album>> getTopAlbums();
    LiveData<List<Track>> getTopTracks();
}
