package com.example.musicplayerapp.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.PlaylistModel;

import java.util.ArrayList;

public class PlaylistOperations {

    public static final String TAG = "playlists Database";
    private static final String[] allColumns = {
            PlaylistDBHelper.COLUMN_ID,
            PlaylistDBHelper.COLUMN_NAME,

    };
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public PlaylistOperations(Context context) {
        dbHandler = new PlaylistDBHelper(context);
    }

    public void open() {
        Log.i(TAG, " Database Opened");
        database = dbHandler.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG, "Database Closed");
        dbHandler.close();
    }

    public void addPlayList(PlaylistModel playlistModel) {
        open();
        ContentValues values = new ContentValues();
        values.put(PlaylistDBHelper.COLUMN_NAME, playlistModel.getName());


        database.insertWithOnConflict(PlaylistDBHelper.TABLE_PLAYLIST, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }

    public ArrayList<PlaylistModel> getAllPlaylists() {
        open();
        Cursor cursor = database.query(PlaylistDBHelper.TABLE_PLAYLIST, allColumns,
                null, null, null, null, null);
        ArrayList<PlaylistModel> playlists = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                PlaylistModel playlist = new PlaylistModel(cursor.getString(cursor.getColumnIndexOrThrow((PlaylistDBHelper.COLUMN_NAME))
                ));
                playlists.add(playlist);
            }
        }
        close();
        return playlists;
    }

    public void removePlaylists() {
        open();


        database.delete(PlaylistDBHelper.TABLE_PLAYLIST, null, null);
        close();
    }


    public void removePlaylist(String name) {
        open();

        String whereClause =
                PlaylistDBHelper.COLUMN_NAME + "=?";
        String[] whereArgs = new String[]{name};

        database.delete(PlaylistDBHelper.TABLE_PLAYLIST, whereClause, whereArgs);
        close();
    }

    public void updatePlaylist(PlaylistModel playlist, String name) {
        open();
        ContentValues values = new ContentValues();
        values.put(PlaylistDBHelper.TABLE_PLAYLIST, name);

        database.updateWithOnConflict(PlaylistDBHelper.TABLE_PLAYLIST, values, PlaylistDBHelper.COLUMN_NAME + " = ?", new String[]{playlist.getName()}, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }


    public boolean isPlaylist(String name) {
        open();
        String Query = "Select * from " + PlaylistDBHelper.TABLE_PLAYLIST + " where " + PlaylistDBHelper.COLUMN_NAME + " = '" + name + "'";
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
