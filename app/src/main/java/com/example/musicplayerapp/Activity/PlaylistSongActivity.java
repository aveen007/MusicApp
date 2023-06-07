package com.example.musicplayerapp.Activity;

import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Adapter.PlaylistAdapeter;
import com.example.musicplayerapp.Adapter.SongPlaylistAdapter;
import com.example.musicplayerapp.DB.PlaylistOperations;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.MyMediaPlayer;
import com.example.musicplayerapp.Model.PlaylistModel;
import com.example.musicplayerapp.R;

import java.util.ArrayList;

public class PlaylistSongActivity extends AppCompatActivity {

    ArrayList<PlaylistModel> playlists;
    RecyclerView recyclerView;
    TextView noPlaylists;
    String currSongPath;
    ImageView addSongPlaylist;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_playlist);
        recyclerView = findViewById(R.id.recyclerViewSongPlaylist);
        noPlaylists = findViewById(R.id.noPlaylists);
        addSongPlaylist = findViewById(R.id.add_song_playlist);
        this.currSongPath = (String) getIntent().getSerializableExtra("song");

        getPlaylistInActivity();

    }

    public void getPlaylistInActivity() {
        PlaylistOperations playlistOperations = new PlaylistOperations(this);

        ArrayList<PlaylistModel> playlists = playlistOperations.getAllPlaylists();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new SongPlaylistAdapter(playlists, this, noPlaylists, this.currSongPath, addSongPlaylist));
    }

}
