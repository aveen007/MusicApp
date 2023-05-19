package com.example.musicplayerapp.Activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayerapp.DB.FavouriteOperations;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.MyMediaPlayer;
import com.example.musicplayerapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {


    FavouriteOperations favouriteOperations = new FavouriteOperations(this);
    boolean isFav = false;
    TextView titleTv, currentTimeTv, totalTimeTv;
    ImageView pausePlay, nextSong, prevSong, musicIcon, favSong;
    SeekBar seekBar;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer myMediaPlayer = MyMediaPlayer.getInstance();


    int a = 0;

    public static String convertToMMSS(String duration) {
        long millis = Long.parseLong(duration);

        return String.format(Locale.getDefault(), "%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }

    void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSong.title);
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));
        pausePlay.setOnClickListener(v -> {
            try {
                pausePlay();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        nextSong.setOnClickListener(v -> playNext());
        prevSong.setOnClickListener(v -> playPrev());
        favSong.setOnClickListener(v -> favourite());
        if (!MyMediaPlayer.getInstance().isPlaying()) {
            playMusic();
        }
    }

    private void playMusic() {

        myMediaPlayer.reset();
        try {
            myMediaPlayer.setDataSource(currentSong.getPath());
            myMediaPlayer.prepare();
            myMediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(myMediaPlayer.getDuration());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    private void pausePlay() throws IOException {
        if (myMediaPlayer.isPlaying()) {
            myMediaPlayer.pause();
        } else {
            myMediaPlayer.prepare();
            myMediaPlayer.start();
        }
    }

    private void playNext() {
        if (MyMediaPlayer.currentIndex == songsList.size() - 1) {
            return;
        }
        MyMediaPlayer.currentIndex += 1;
        myMediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPrev() {
        if (MyMediaPlayer.currentIndex == 0) {
            return;
        }
        MyMediaPlayer.currentIndex -= 1;
        myMediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void favourite() {


        if (!isFav) {


            favouriteOperations.addSongFav(currentSong);

        } else {

            favouriteOperations.removeSong(currentSong);

        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        titleTv = findViewById(R.id.songTitle);
        totalTimeTv = findViewById(R.id.total_time);
        currentTimeTv = findViewById(R.id.current_time);
        seekBar = findViewById(R.id.seekbar);
        pausePlay = findViewById(R.id.play_pause);
        nextSong = findViewById(R.id.next);
        prevSong = findViewById(R.id.previous);
        musicIcon = findViewById(R.id.musicIcon);
        favSong = findViewById(R.id.favorite_song);

        titleTv.setSelected(true);
        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");


        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myMediaPlayer != null) {
                    setResourcesWithMusic();
                    seekBar.setProgress(myMediaPlayer.getCurrentPosition());
                    Log.wtf("time ", convertToMMSS(myMediaPlayer.getCurrentPosition() + ""));
                    currentTimeTv.setText(convertToMMSS(myMediaPlayer.getCurrentPosition() + ""));
                    if (myMediaPlayer.isPlaying()) {
                        pausePlay.setImageResource(R.drawable.baseline_pause_circle_outline_24);
                        musicIcon.setRotation(a++);
                    } else {
                        pausePlay.setImageResource(R.drawable.baseline_play_circle_outline_24);
                        musicIcon.setRotation(0);

                    }


                }

                new Handler().postDelayed(this, 100);

            }

        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (myMediaPlayer != null && b) {
                    myMediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @Override
    protected void onResume() {


        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myMediaPlayer != null) {
                    isFav = favouriteOperations.isFavourite(currentSong.title);


                    if (isFav) {
                        favSong.setImageResource(R.drawable.baseline_favorite_24);

                    } else {
                        favSong.setImageResource(R.drawable.baseline_favorite_border_24);

                    }

                }
                new Handler().postDelayed(this, 100);


            }
        });
        super.onResume();
    }


}