package com.example.musicplayerapp.Model;

import java.io.Serializable;

public class PlaylistModel implements Serializable {


    String name;

    public PlaylistModel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
