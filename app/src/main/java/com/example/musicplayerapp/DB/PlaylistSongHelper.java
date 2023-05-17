package com.example.musicplayerapp.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class PlaylistSongHelper extends SQLiteOpenHelper {


    public static final String TABLE_PLAYLISTSONG = "playlistSong";
    public static final String COLUMN_ID = "playlistSongID";
    public static final String COLUMN_PLAYLISTNAME = "playlistName";
    public static final String COLUMN_SONGPATH = "songPath";

    private static final String DATABASE_NAME = "songs.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_PLAYLISTSONG + " (" + COLUMN_ID
            + " INTEGER PRIMARY KEY, " +

            " FOREIGN KEY (" + COLUMN_PLAYLISTNAME + ")" +
            " REFERENCES " + PlaylistDBHelper.TABLE_PLAYLIST + "(" + PlaylistDBHelper.COLUMN_NAME + ")," +

            " FOREIGN KEY (" + COLUMN_SONGPATH + ")" +
            " REFERENCES " + SongDBHelper.TABLE_SONGS + "(" + SongDBHelper.COLUMN_PATH + ")" +

            ")";

    public PlaylistSongHelper(Context context) {

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
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTSONG);
        db.execSQL(TABLE_CREATE);
    }
}
