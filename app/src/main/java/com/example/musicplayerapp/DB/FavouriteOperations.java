package com.example.musicplayerapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.musicplayerapp.Model.AudioModel;

import java.util.ArrayList;

public class FavouriteOperations {

    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public FavouriteOperations(Context context) {
        dbHandler = new SongDBHelper(context);
    }

    public void open() {
        //   Log.i(TAG, " Database Opened");
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        //   Log.i(TAG, "Database Closed");
        dbHandler.close();
    }

    public void addSongFav(AudioModel song) {
        open();
        song.isFavourite = "1";
        ContentValues values = new ContentValues();
        values.put(SongDBHelper.COLUMN_TITLE, song.getTitle());
        values.put(SongDBHelper.COLUMN_DURATION, song.getDuration());

        values.put(SongDBHelper.COLUMN_ARTIST, song.getArtist());
        values.put(SongDBHelper.COLUMN_ALBUM, song.getAlbum());
        values.put(SongDBHelper.COLUMN_FAVOURITE, "1");
        values.put(SongDBHelper.COLUMN_PATH, song.getPath());
        database.updateWithOnConflict(SongDBHelper.TABLE_SONGS, values, SongDBHelper.COLUMN_PATH + " = ?", new String[]{song.getPath()}, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }

    public ArrayList<AudioModel> getAllFavorites() {
        open();
        Cursor cursor = database.rawQuery("select * from " + SongDBHelper.TABLE_SONGS + " WHERE " + SongDBHelper.COLUMN_FAVOURITE + " =?", new String[]{"1"});
        ArrayList<AudioModel> favSongs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AudioModel song = new AudioModel(cursor.getString(cursor.getColumnIndexOrThrow((SongDBHelper.COLUMN_PATH))), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_DURATION)), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_ARTIST)), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_ALBUM)), cursor.getString(cursor.getColumnIndexOrThrow(SongDBHelper.COLUMN_FAVOURITE)));
                favSongs.add(song);
            }
        }
        cursor.close();
        close();
        return favSongs;
    }

    public void removeSong(AudioModel song) {
        open();
        song.isFavourite = "0";
        ContentValues values = new ContentValues();
        values.put(SongDBHelper.COLUMN_TITLE, song.getTitle());
        values.put(SongDBHelper.COLUMN_DURATION, song.getDuration());

        values.put(SongDBHelper.COLUMN_ARTIST, song.getArtist());
        values.put(SongDBHelper.COLUMN_ALBUM, song.getAlbum());
        values.put(SongDBHelper.COLUMN_FAVOURITE, "0");
        values.put(SongDBHelper.COLUMN_PATH, song.getPath());
        database.updateWithOnConflict(SongDBHelper.TABLE_SONGS, values, SongDBHelper.COLUMN_PATH + " = ?", new String[]{song.getPath()}, SQLiteDatabase.CONFLICT_REPLACE);
        close();
    }

    public boolean isFavourite(String title) {
        open();
        String Query = "Select * from " + SongDBHelper.TABLE_SONGS + " where " + SongDBHelper.COLUMN_TITLE + " =? and " + SongDBHelper.COLUMN_FAVOURITE + " =? ";
        Cursor cursor = database.rawQuery(Query, new String[]{title, "1"});
        boolean ret;
        ret = cursor.getCount() > 0;
        cursor.close();

        return ret;
    }
}
