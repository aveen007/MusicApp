package com.example.musicplayerapp.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.musicplayerapp.Model.AudioModel;

import java.util.ArrayList;

public class SongOperations {

    public static final String TAG = "Songs Database";
    private static final String[] allColumns = {
            SongDBHelper.COLUMN_ID,
            SongDBHelper.COLUMN_TITLE,
            SongDBHelper.COLUMN_DURATION,
            SongDBHelper.COLUMN_ARTIST,
            SongDBHelper.COLUMN_ALBUM,
            SongDBHelper.COLUMN_FAVOURITE,
            SongDBHelper.COLUMN_PATH,
    };
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public SongOperations(Context context) {
        dbHandler = new SongDBHelper(context);
    }

    public void open() {
        Log.i(TAG, " Database Opened");
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG, "Database Closed");
        dbHandler.close();
    }

    public void addSong(AudioModel song) {
        open();
        ContentValues values = new ContentValues();
        values.put(SongDBHelper.COLUMN_TITLE, song.getTitle());
        values.put(SongDBHelper.COLUMN_DURATION, song.getDuration());

        values.put(SongDBHelper.COLUMN_ARTIST, song.getArtist());
        values.put(SongDBHelper.COLUMN_ALBUM, song.getAlbum());
        values.put(SongDBHelper.COLUMN_FAVOURITE, song.getIsFavourite());
        values.put(SongDBHelper.COLUMN_PATH, song.getPath());
        database.insertWithOnConflict(SongDBHelper.TABLE_SONGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }

    public ArrayList<AudioModel> getAllSongs() {
        open();
        Cursor cursor = database.query(SongDBHelper.TABLE_SONGS, allColumns,
                null, null, null, null, null);
        ArrayList<AudioModel> songs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AudioModel song = new AudioModel(cursor.getString(cursor.getColumnIndexOrThrow((SongDBHelper.COLUMN_PATH))
                ), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_TITLE))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_DURATION))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_ARTIST))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_ALBUM))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_FAVOURITE)));
                songs.add(song);
            }
        }
        close();
        return songs;
    }

    public ArrayList<AudioModel> getAllSongsByGroup(String group) {
        open();
        String Query = "Select * from " + SongDBHelper.TABLE_SONGS + " group by " + group;

        Cursor cursor = database.rawQuery(Query, null);
        ArrayList<AudioModel> songs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AudioModel song = new AudioModel(cursor.getString(cursor.getColumnIndexOrThrow((SongDBHelper.COLUMN_PATH))
                ), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_TITLE))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_DURATION))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_ARTIST))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_ALBUM))
                        , cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_FAVOURITE)));
                songs.add(song);
            }
        }
        close();
        return songs;
    }

    public void removeSong(String songPath) {
        open();
        String whereClause =
                SongDBHelper.COLUMN_PATH + "=?";
        String[] whereArgs = new String[]{songPath};

        database.delete(SongDBHelper.TABLE_SONGS, whereClause, whereArgs);
        close();
    }

    public void updateSong(AudioModel song) {
        open();
        ContentValues values = new ContentValues();
        values.put(SongDBHelper.COLUMN_TITLE, song.getTitle());
        values.put(SongDBHelper.COLUMN_DURATION, song.getDuration());

        values.put(SongDBHelper.COLUMN_ARTIST, song.getArtist());
        values.put(SongDBHelper.COLUMN_ALBUM, song.getAlbum());
        values.put(SongDBHelper.COLUMN_FAVOURITE, song.getAlbum());
        values.put(SongDBHelper.COLUMN_PATH, song.getPath());
        database.updateWithOnConflict(SongDBHelper.TABLE_SONGS, values, SongDBHelper.COLUMN_PATH + " = ?", new String[]{song.getPath()}, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }


    public boolean isSong(String title) {
        open();
        String Query = "Select * from " + SongDBHelper.TABLE_SONGS + " where " + SongDBHelper.COLUMN_TITLE + " = '" + title + "'";
        Cursor cursor = database.rawQuery(Query, null);
        boolean ret;
        if (cursor.getCount() <= 0) {

            ret = false;
        } else {
            ret = true;
        }
        cursor.close();
        return ret;
    }

}






















