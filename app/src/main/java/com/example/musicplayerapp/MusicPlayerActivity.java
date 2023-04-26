package com.example.musicplayerapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv, currentTimeTv, totalTimeTv;
    ImageView pausePlay, nextSong, prevSong, musicIcon;
    SeekBar seekBar;
    ArrayList<AudioModel> songsList;
    AudioModel currentSong;
    MediaPlayer myMediaPlayer = MyMediaPlayer.getInstance();
    int a = 0;

    public static String convertToMMSS(String duration) {
        Long millies = Long.parseLong(duration);

        String s = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millies) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millies) % TimeUnit.MINUTES.toSeconds(1));
        return s;
    }

    void setResourcesWithMusic() {
        currentSong = songsList.get(MyMediaPlayer.currentIndex);
        titleTv.setText(currentSong.title);
        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));
        pausePlay.setOnClickListener(v -> pausePlay());
        nextSong.setOnClickListener(v -> playNext());
        prevSong.setOnClickListener(v -> playPrev());
        playMusic();
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

    private void pausePlay() {
        if (myMediaPlayer.isPlaying()) {
            myMediaPlayer.pause();
        } else {
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

        titleTv.setSelected(true);
        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");
        setResourcesWithMusic();

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (myMediaPlayer != null) {
                    seekBar.setProgress(myMediaPlayer.getCurrentPosition());
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
}