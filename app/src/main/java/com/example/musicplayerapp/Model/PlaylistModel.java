package com.example.musicplayerapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaylistModel implements Serializable {


    String name;
    String id;
    ArrayList<AudioModel> playlistSongs;

    public PlaylistModel(String name) {
        this.name = name;
        playlistSongs = new ArrayList<AudioModel>();
    }

    public String getName() {
        return name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<AudioModel> getPlaylistSongs() {
        return playlistSongs;
    }

    public void setPlaylistSongs(ArrayList<AudioModel> playlistSongs) {
        this.playlistSongs = playlistSongs;
    }

    public void AddPlaylistSong(AudioModel song) {
        playlistSongs.add(song);
    }

    public void RemovePlaylistSong(AudioModel song) {

        playlistSongs.remove(song);
    }
}
