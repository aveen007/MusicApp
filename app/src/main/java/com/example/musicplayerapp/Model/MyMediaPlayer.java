package com.example.musicplayerapp.Model;

import android.media.MediaPlayer;

import java.util.ArrayList;

public class MyMediaPlayer {

    public static int currentIndex = -1;
    static MediaPlayer instance;

    public static MediaPlayer getInstance() {
        if (instance == null) {
            instance = new MediaPlayer();

        }
        return instance;
    }

}
