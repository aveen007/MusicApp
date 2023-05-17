package com.example.musicplayerapp.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.example.musicplayerapp.Adapter.MusicListAdapter;
import com.example.musicplayerapp.DB.FavouriteOperations;
import com.example.musicplayerapp.DB.SongDBHelper;
import com.example.musicplayerapp.DB.SongOperations;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolBar;
    ImageView favourites, library;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    TextView noMusicTextView, songsTV;
    RecyclerView recyclerView;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    ArrayList<AudioModel> favSongsList = new ArrayList<>();
    SongOperations songOperations = new SongOperations(this);
    private Menu toolbarMenu;
    private String searchText = "";
    private String Artist = "";
    private String Album = "";
    private ViewBy viewBy = ViewBy.SONG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase("DATABASE_NAME");
        recyclerView = findViewById(R.id.recyclerView);
        noMusicTextView = findViewById(R.id.noSongsText);
        songsTV = (TextView) findViewById(R.id.songsText);
        drawerLayout = findViewById(R.id.drawer);
        favourites = findViewById(R.id.favourite);
        favourites.setOnClickListener(v -> displayFavourites());
        library = findViewById(R.id.library);
        library.setOnClickListener(v -> displayLibrary());
        songsTV.setText("Songs");
        toolBar = findViewById(R.id.toolbar);
        toolBar.setNavigationIcon(R.drawable.baseline_menu_24);
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // setSupportActionBar(toolBar);
        navigationView = findViewById(R.id.nav_view);
        if (checkPermission() == false) {
            requestPermission();
            return;
        }
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.IS_FAVORITE,


        };
        String selection = MediaStore.Audio.Media.IS_MUSIC + " !=0";
        ArrayList<AudioModel> sng = new ArrayList<>();
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel song = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            if (new File(song.getPath()).exists())
                songsList.add(song);
            songOperations.addSong(song);
        }

        // sng = songOperations.getAllSongs();


        Artist = (String) getIntent().getSerializableExtra("ARTIST");
        Album = (String) getIntent().getSerializableExtra("ALBUM");
        if (Artist != null) {


            songsList = (ArrayList<AudioModel>) songsList.stream().filter(s -> s.artist.equals(Artist)).collect(Collectors.toList());

        }
        if (Album != null) {


            songsList = (ArrayList<AudioModel>) songsList.stream().filter(s -> s.album.equals(Album)).collect(Collectors.toList());

        }
        // viewBy = ViewBy.SONG;

        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new MusicListAdapter(songsList, viewBy, getApplicationContext()));
        }


        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_view_by, R.string.close_view_by);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(MainActivity.this, "Read permissions are required, please allow from settings", Toast.LENGTH_SHORT).show();

        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 123);

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        songsTV.setText("Songs");

        if (recyclerView != null) {
            recyclerView.setAdapter(new MusicListAdapter(songsList, viewBy, getApplicationContext()));
        }
    }

    private void displayFavourites() {

        FavouriteOperations favouriteOperations = new FavouriteOperations(this);
        favSongsList = favouriteOperations.getAllFavorites();
        if (favSongsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            noMusicTextView.setVisibility(View.GONE);

        }
        songsTV.setText("Favourite Songs");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MusicListAdapter(favSongsList, viewBy, getApplicationContext()));
    }

    private void displayLibrary() {
        songsTV.setText("Songs");
        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            noMusicTextView.setVisibility(View.GONE);

        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MusicListAdapter(songsList, viewBy, getApplicationContext()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.toolbarMenu = menu;
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchText = queryText();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;

                searchText = queryText();
                filter(searchText);
                //setPagerLayout();
                return true;
            }
        });

        return true;

    }

    private void filter(String s) {
        ArrayList<AudioModel> searchedSongsList = new ArrayList<>();
        for (AudioModel item : songsList) {
            if (item.getTitle().toLowerCase().contains(s)) {
                searchedSongsList.add(item);

            }


        }
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MusicListAdapter(searchedSongsList, viewBy, getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }

    public String queryText() {
        return searchText.toLowerCase();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        item.setChecked(true);
        drawerLayout.closeDrawers();
        switch (item.getItemId()) {
            case R.id.byAlbum:
                this.viewBy = ViewBy.ALBUM;
                break;
            case R.id.byArtist:
                this.viewBy = ViewBy.ARTIST;
                break;
            case R.id.bySong:
                this.viewBy = ViewBy.SONG;
                break;
        }
        setViewBy();
        return true;
    }

    public void setViewBy() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<AudioModel> groupedSongs = songOperations.getAllSongsByGroup(viewBy.toString());
        recyclerView.setAdapter(new MusicListAdapter(groupedSongs, viewBy, getApplicationContext()));

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}