package com.example.musicplayerapp.Activity;

public enum ViewBy {

    ARTIST("artist"), ALBUM("album"), SONG("title");

    public String name;

    ViewBy(String name) {
        this.name = name;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }

}
