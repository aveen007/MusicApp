package com.example.musicplayerapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SongDBHelper extends SQLiteOpenHelper {


    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_ID = "songID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_PATH = "songpath";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_FAVOURITE = "favourite";
    private static final String DATABASE_NAME = "songs2.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_SONGS + " (" + COLUMN_ID
            + " INTEGER, " + COLUMN_TITLE
            + " TEXT, " + COLUMN_DURATION
            + " TEXT, " + COLUMN_ARTIST
            + " TEXT, " + COLUMN_ALBUM
            + " TEXT, " + COLUMN_FAVOURITE

            + " TEXT, " + COLUMN_PATH + " TEXT PRIMARY KEY " + ")";

    public SongDBHelper(Context context) {

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        db.execSQL(TABLE_CREATE);
    }


}
