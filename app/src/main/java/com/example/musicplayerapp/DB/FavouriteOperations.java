package com.example.musicplayerapp.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.musicplayerapp.Model.AudioModel;

import java.util.ArrayList;

public class FavouriteOperations {

    public static final String TAG = "Favorites Database";
    private static final String[] allColumns = {
            FavouriteDBHelper.COLUMN_ID,
            FavouriteDBHelper.COLUMN_TITLE,
            FavouriteDBHelper.COLUMN_DURATION,
            FavouriteDBHelper.COLUMN_PATH
    };
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public FavouriteOperations(Context context) {
        dbHandler = new FavouriteDBHelper(context);
    }

    public void open() {
        Log.i(TAG, " Database Opened");
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG, "Database Closed");
        dbHandler.close();
    }

    public void addSongFav(AudioModel song) {
        open();
        ContentValues values = new ContentValues();
        values.put(FavouriteDBHelper.COLUMN_TITLE, song.getTitle());
        values.put(FavouriteDBHelper.COLUMN_DURATION, song.getDuration());
        values.put(FavouriteDBHelper.COLUMN_PATH, song.getPath());

        database.insertWithOnConflict(FavouriteDBHelper.TABLE_SONGS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }

    public ArrayList<AudioModel> getAllFavorites() {
        open();
        Cursor cursor = database.query(FavouriteDBHelper.TABLE_SONGS, allColumns,
                null, null, null, null, null);
        ArrayList<AudioModel> favSongs = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                AudioModel song = new AudioModel(cursor.getString(cursor.getColumnIndexOrThrow((FavouriteDBHelper.COLUMN_PATH))
                ), cursor.getString(cursor.getColumnIndexOrThrow(FavouriteDBHelper.COLUMN_TITLE)), cursor.getString(cursor.getColumnIndexOrThrow(FavouriteDBHelper.COLUMN_DURATION)));
                favSongs.add(song);
            }
        }
        close();
        return favSongs;
    }

    public void removeSong(String songPath) {
        open();
        String whereClause =
                FavouriteDBHelper.COLUMN_PATH + "=?";
        String[] whereArgs = new String[]{songPath};

        database.delete(FavouriteDBHelper.TABLE_SONGS, whereClause, whereArgs);
        close();
    }

    public boolean isFavourite(String title) {
        open();
        String Query = "Select * from " + FavouriteDBHelper.TABLE_SONGS + " where " + FavouriteDBHelper.COLUMN_TITLE + " = '" + title + "'";
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






















