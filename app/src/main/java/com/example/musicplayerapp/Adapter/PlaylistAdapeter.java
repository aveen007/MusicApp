package com.example.musicplayerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Activity.EditPlaylistDialogue;
import com.example.musicplayerapp.Activity.MainActivity;
import com.example.musicplayerapp.Activity.MusicPlayerActivity;
import com.example.musicplayerapp.Activity.PlaylistDialog;
import com.example.musicplayerapp.Activity.ViewBy;
import com.example.musicplayerapp.DB.PlaylistOperations;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.MyMediaPlayer;
import com.example.musicplayerapp.Model.PlaylistModel;
import com.example.musicplayerapp.R;

import java.util.ArrayList;

public class PlaylistAdapeter extends RecyclerView.Adapter<PlaylistAdapeter.ViewHolderPlaylist> implements PopupMenu.OnMenuItemClickListener {

    public int currentPos;
    ArrayList<PlaylistModel> playlists;
    Context context;

    TextView noMusic;

    public PlaylistAdapeter(ArrayList<PlaylistModel> songsList, Context context, TextView noMusic) {
        this.playlists = songsList;
        this.context = context;
        this.noMusic = noMusic;
        if (playlists.size() == 0) {
            this.noMusic.setVisibility(View.VISIBLE);
        } else {
            this.noMusic.setVisibility(View.GONE);

        }

    }

    @NonNull
    @Override
    public PlaylistAdapeter.ViewHolderPlaylist onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_playlist, parent, false);

        return new PlaylistAdapeter.ViewHolderPlaylist(view);
    }


    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapeter.ViewHolderPlaylist holder, int position) {
        PlaylistModel playlistData = playlists.get(position);


        holder.titleTextView.setText(playlistData.getName());


        holder.itemView.setOnClickListener(v ->

        {
            //navigate to another activity

            //  MyMediaPlayer.getInstance().reset();
            //  MyMediaPlayer.currentIndex = holder.getAdapterPosition();
//            Intent intent = new Intent(context, MusicPlayerActivity.class);
//            intent.putExtra("LIST", playlists);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            context.startActivity(intent);

            // currentPos = holder.getAdapterPosition();
        });
        holder.more.setOnClickListener(v -> ShowMore(v, holder.getAdapterPosition()));
    }


    public void ShowMore(View view, int pos) {

        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.more_playlist);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();
        currentPos = pos;
    }

    //TODO: the color red is being put to the wrong index when switching between view Bys
    @Override
    public int getItemCount() {
        return playlists.size();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.Edit:
                String oldPlaylistName = playlists.get(currentPos).getName();
                EditPlayList(oldPlaylistName);
                break;
            case R.id.Delete:

                DeletePlayList(currentPos);
                break;
        }
        return false;
    }

    public void EditPlayList(String oldPlaylistName) {
        EditPlaylistDialogue playlistDialog = new EditPlaylistDialogue(oldPlaylistName);
        FragmentActivity fragmentActivity = (FragmentActivity) (context);
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        playlistDialog.show(fragmentManager, "edit playlist");


    }

    public void DeletePlayList(int index) {
        PlaylistOperations playlistOperations = new PlaylistOperations(this.context);
        //   playlistOperations.removePlaylists();
        playlistOperations.removePlaylist(playlists.get(index).getName());
        playlists.remove(index);
        notifyItemRemoved(index);
        if (playlists.size() == 0) {
            this.noMusic.setVisibility(View.VISIBLE);
        } else {
            this.noMusic.setVisibility(View.GONE);

        }
    }

    public static class ViewHolderPlaylist extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageIcon, more;

        //TODO: when first in with empty list then no music doesnt appear and
        //when deleted all , doesnt show unless we move back
        //
        public ViewHolderPlaylist(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.playlistTitle);
            imageIcon = itemView.findViewById(R.id.iconView);
            more = itemView.findViewById(R.id.morePlaylist);
        }

    }


}
