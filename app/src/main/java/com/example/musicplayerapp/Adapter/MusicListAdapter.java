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
import com.example.musicplayerapp.R;

import java.util.ArrayList;

public class MusicListAdapter extends RecyclerView.Adapter<MusicListAdapter.ViewHolder> {

    ArrayList<AudioModel> songsList;
    ViewBy viewBy;
    Context context;

    public MusicListAdapter(ArrayList<AudioModel> songsList, ViewBy viewBy, Context context) {
        this.songsList = songsList;
        this.context = context;
        this.viewBy = viewBy;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicListAdapter.ViewHolder holder, int position) {
        AudioModel songData = songsList.get(position);

        switch (viewBy) {
            case ALBUM:
                holder.titleTextView.setText(songData.getAlbum());
                break;
            case ARTIST:
                holder.titleTextView.setText(songData.getArtist());

                break;
            case SONG:
                holder.titleTextView.setText(songData.getTitle());

                break;
        }

        if (viewBy.equals(ViewBy.SONG)) {
            if (MyMediaPlayer.currentIndex == position) {
                holder.titleTextView.setTextColor(Color.parseColor("#FF0000"));
            } else {
                holder.titleTextView.setTextColor(Color.parseColor("#000000"));
            }
        }
        holder.itemView.setOnClickListener(v -> {
            //navigate to another activity

            switch (viewBy) {
                case SONG:

                    MyMediaPlayer.getInstance().reset();
                    MyMediaPlayer.currentIndex = holder.getAdapterPosition();
                    Intent intent = new Intent(context, MusicPlayerActivity.class);
                    intent.putExtra("LIST", songsList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    break;
                case ARTIST:

                    Intent intent2 = new Intent(context, MainActivity.class);
                    intent2.putExtra("ARTIST", songsList.get(position).getArtist());
                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                    break;
                case ALBUM:

                    Intent intent3 = new Intent(context, MainActivity.class);
                    intent3.putExtra("ALBUM", songsList.get(position).getAlbum());
                    intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent3);
                    break;

            }


        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageIcon;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.musicTitle);
            imageIcon = itemView.findViewById(R.id.iconView);
        }

    }


}
