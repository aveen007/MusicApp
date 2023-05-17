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

public class PlayListSongOperations {

    public static final String TAG = "playlists Database";
    private static final String[] allColumns = {
            PlaylistSongHelper.COLUMN_ID,
            PlaylistSongHelper.COLUMN_PLAYLISTNAME,
            PlaylistSongHelper.COLUMN_SONGPATH,

    };
    SQLiteOpenHelper dbHandler;
    SQLiteDatabase database;

    public PlayListSongOperations(Context context) {
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

    public void addPlayListSong(PlaylistModel playlist, AudioModel song) {
        open();
        ContentValues values = new ContentValues();
        values.put(PlaylistSongHelper.COLUMN_PLAYLISTNAME, playlist.getName());
        values.put(PlaylistSongHelper.COLUMN_SONGPATH, song.getPath());


        database.insertWithOnConflict(PlaylistDBHelper.TABLE_PLAYLIST, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        close();
    }

    public ArrayList<AudioModel> getAllPlaylistSongs(PlaylistModel playlist) {
        open();
        Cursor cursor = database.rawQuery("select * from " + PlaylistSongHelper.TABLE_PLAYLISTSONG + " WHERE " + PlaylistSongHelper.COLUMN_PLAYLISTNAME + " =?", new String[]{playlist.getName()});
        ArrayList<AudioModel> playlistSong = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                String songPath = cursor.getString(cursor.getColumnIndexOrThrow((SongDBHelper.COLUMN_PATH)));
                Cursor cursorSong = database.rawQuery("select * from " + SongDBHelper.TABLE_SONGS + " WHERE " + SongDBHelper.COLUMN_PATH + " =?", new String[]{songPath});
                if (cursorSong.getCount() > 0) {
                    while (cursorSong.moveToNext()) {

                        AudioModel song = new AudioModel(cursorSong.getString(cursorSong.getColumnIndexOrThrow((SongDBHelper.COLUMN_PATH))
                        ), cursorSong.getString(cursorSong.getColumnIndexOrThrow(SongDBHelper.COLUMN_TITLE))
                                , cursorSong.getString(cursorSong.getColumnIndexOrThrow(SongDBHelper.COLUMN_DURATION))
                                , cursorSong.getString(cursorSong.getColumnIndexOrThrow(SongDBHelper.COLUMN_ARTIST))
                                , cursorSong.getString(cursorSong.getColumnIndexOrThrow(SongDBHelper.COLUMN_ALBUM))
                                , cursorSong.getString(cursorSong.getColumnIndexOrThrow(SongDBHelper.COLUMN_FAVOURITE)));
                        playlistSong.add(song);
                    }
                }
            }
        }
        close();
        return playlistSong;
    }

    public void removePlaylistSong(String playlistName, String songPath) {
        open();
        String whereClause =
                PlaylistSongHelper.COLUMN_PLAYLISTNAME + "=? AND " + PlaylistSongHelper.COLUMN_SONGPATH + "=?";
        String[] whereArgs = new String[]{playlistName, songPath};

        database.delete(PlaylistDBHelper.TABLE_PLAYLIST, whereClause, whereArgs);
        close();
    }


    public boolean isPlaylistSong(String playlistName, String songPath) {
        open();
        String whereClause =
                PlaylistSongHelper.COLUMN_PLAYLISTNAME + "=? AND " + PlaylistSongHelper.COLUMN_SONGPATH + "=?";
        String[] whereArgs = new String[]{playlistName, songPath};

        String Query = "Select * from " + PlaylistDBHelper.TABLE_PLAYLIST + " where " + whereClause;
        Cursor cursor = database.rawQuery(Query, whereArgs);
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
