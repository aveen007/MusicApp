package com.example.musicplayerapp.Activity;

import androidx.annotation.NonNull;

public enum ViewBy {

    ARTIST("artist"), ALBUM("album"), SONG("title");

    public final String name;

    ViewBy(String name) {
        this.name = name;
    }

    @NonNull
    public String toString() {
        return this.name;
    }

}
