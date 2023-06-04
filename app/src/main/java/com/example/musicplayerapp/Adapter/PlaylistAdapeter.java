package com.example.musicplayerapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Activity.MainActivity;
import com.example.musicplayerapp.Activity.MusicPlayerActivity;
import com.example.musicplayerapp.Activity.ViewBy;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.MyMediaPlayer;
import com.example.musicplayerapp.Model.PlaylistModel;
import com.example.musicplayerapp.R;

import java.util.ArrayList;

public class PlaylistAdapeter extends RecyclerView.Adapter<PlaylistAdapeter.ViewHolderPlaylist> {

    ArrayList<PlaylistModel> playlists;
    ViewBy viewBy;
    Context context;

    public PlaylistAdapeter(ArrayList<PlaylistModel> songsList, Context context) {
        this.playlists = songsList;
        this.context = context;

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

            MyMediaPlayer.getInstance().reset();
            MyMediaPlayer.currentIndex = holder.getAdapterPosition();
            Intent intent = new Intent(context, MusicPlayerActivity.class);
            intent.putExtra("LIST", playlists);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);


        });
    }


    //TODO: the color red is being put to the wrong index when switching between view Bys
    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class ViewHolderPlaylist extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageIcon;


        public ViewHolderPlaylist(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.musicTitle);
            imageIcon = itemView.findViewById(R.id.iconView);
        }

    }


}
