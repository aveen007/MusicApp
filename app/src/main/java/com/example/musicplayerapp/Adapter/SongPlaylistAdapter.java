package com.example.musicplayerapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Activity.EditPlaylistDialogue;
import com.example.musicplayerapp.DB.PlayListSongOperations;
import com.example.musicplayerapp.DB.PlaylistOperations;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.PlaylistModel;
import com.example.musicplayerapp.R;

import java.util.ArrayList;

public class SongPlaylistAdapter extends RecyclerView.Adapter<SongPlaylistAdapter.ViewHolderSongPlaylist> {

    public int currentPos;
    ArrayList<PlaylistModel> playlists;
    ArrayList<PlaylistModel> playlistsSong;
    ArrayList<PlaylistModel> playlistsOld;
    Context context;
    TextView noMusic;
    AudioModel currSong;
    ImageView addSongPlaylist;


    public SongPlaylistAdapter(ArrayList<PlaylistModel> songsList, Context context, TextView noMusic, String currSongPath, ImageView addSongPlaylist) {
        this.playlists = songsList;
        this.context = context;
        this.noMusic = noMusic;
        this.addSongPlaylist = addSongPlaylist;
        PlayListSongOperations playListSongOperations = new PlayListSongOperations(this.context);
        this.currSong = playListSongOperations.getSong(currSongPath);
        // this.playlistsSong = playListSongOperations.getAllSongPlaylists(currSong);

        this.playlistsOld = playListSongOperations.getAllSongPlaylists(currSong);

        if (playlists.size() == 0) {
            this.noMusic.setVisibility(View.VISIBLE);
        } else {
            this.noMusic.setVisibility(View.GONE);

        }

    }

    @NonNull
    @Override
    public SongPlaylistAdapter.ViewHolderSongPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_song_playlist, parent, false);

        return new SongPlaylistAdapter.ViewHolderSongPlaylist(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SongPlaylistAdapter.ViewHolderSongPlaylist holder, int position) {
        PlaylistModel playlistData = playlists.get(position);


        holder.titleTextView.setText(playlistData.getName());

        PlaylistModel playlistModel = new PlaylistModel((String) holder.titleTextView.getText());
        PlayListSongOperations playListSongOperations = new PlayListSongOperations(this.context);
        boolean isPlaylistSong = playListSongOperations.isPlaylistSong(playlistData.getName(), currSong.getPath());

        boolean lets = playlistsOld.stream()
                .anyMatch(obj -> obj.getName().equals(playlistModel.getName()));
        if (lets) {
            holder.checkBox.setChecked(true);
        }
        addSongPlaylist.setOnClickListener(v -> AddToPlaylist(v));

        holder.checkBox.setOnClickListener(v -> {

            boolean isCheckedP = holder.checkBox.isChecked();
            if (playlistsOld != null) {
                if (isPlaylistSong && !isCheckedP) {
                    playlistsOld.removeIf(obj -> obj.getName().equals(playlistModel.getName()));
                    ;

                } else {
                    if (!isPlaylistSong && isCheckedP)
                        playlistsOld.add(playlistModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }


    public void AddToPlaylist(View view) {

        PlayListSongOperations playListSongOperations = new PlayListSongOperations(this.context);
        playListSongOperations.removeAllSongPlaylists(currSong.getPath());
        for (PlaylistModel playlist : playlistsOld) {

            playListSongOperations.addPlayListSong(playlist, currSong);


        }
        notifyDataSetChanged();


    }


    public static class ViewHolderSongPlaylist extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageIcon;

        CheckBox checkBox;


        public ViewHolderSongPlaylist(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.playlistTitle);
            imageIcon = itemView.findViewById(R.id.iconView);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

    }


}

