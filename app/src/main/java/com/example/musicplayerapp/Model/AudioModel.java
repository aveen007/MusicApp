package com.example.musicplayerapp.Model;

import java.io.Serializable;

public class AudioModel implements Serializable {
    public String path;
    public String title;
    public String duration;
    public String artist;
    public String album;
    public String isFavourite;

    public AudioModel(String path, String title, String duration, String artist, String album, String isFavourite) {
        this.path = path;
        this.title = title;
        this.duration = duration;
        this.artist = artist;
        this.album = album;
        this.isFavourite = isFavourite;

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }
}
