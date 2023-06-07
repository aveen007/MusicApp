package com.example.musicplayerapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistDBHelper {


    public static final String TABLE_PLAYLIST = "playlist";
    public static final String COLUMN_ID = "playlistID";
    public static final String COLUMN_NAME = "playlistName";


    public static final String TABLE_CREATE_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLIST + " (" + COLUMN_ID
            + " INTEGER, " + COLUMN_NAME + " TEXT PRIMARY KEY " + ")";


}
