package com.example.musicplayerapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistDBHelper extends SQLiteOpenHelper {


    public static final String TABLE_PLAYLIST = "playlist";
    public static final String COLUMN_ID = "playlistID";
    public static final String COLUMN_NAME = "playlistName";

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_PLAYLIST + " (" + COLUMN_ID
            + " INTEGER, " + COLUMN_NAME + " TEXT PRIMARY KEY " + ")";

    public PlaylistDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // getReadableDatabase();
        Log.i("DB", "dbManager");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL(TABLE_CREATE);
    }

}
