package com.example.musicplayerapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistSongHelper {


    public static final String TABLE_PLAYLISTSONG = "playlistSong";
    public static final String COLUMN_ID = "playlistSongID";
    public static final String COLUMN_PLAYLISTNAME = "playlistName";
    public static final String COLUMN_SONGPATH = "songPath";
    public static final String TABLE_CREATE_SONG_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLISTSONG + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY, " +

            COLUMN_PLAYLISTNAME + " TEXT " +
            " REFERENCES " + PlaylistDBHelper.TABLE_PLAYLIST + " (" + PlaylistDBHelper.COLUMN_NAME + ")" +
            "ON DELETE CASCADE" +
            "," +

            COLUMN_SONGPATH + " TEXT " +
            " REFERENCES " + SongDBHelper.TABLE_SONGS + " (" + SongDBHelper.COLUMN_PATH + ")" +

            " ON DELETE CASCADE)";


}
