package com.example.musicplayerapp.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerapp.Adapter.MusicListAdapter;
import com.example.musicplayerapp.Adapter.PlaylistAdapeter;
import com.example.musicplayerapp.DB.FavouriteOperations;
import com.example.musicplayerapp.DB.PlaylistOperations;
import com.example.musicplayerapp.DB.SongOperations;
import com.example.musicplayerapp.Model.AudioModel;
import com.example.musicplayerapp.Model.PlaylistModel;
import com.example.musicplayerapp.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PlaylistDialog.PlaylistDialogListener, EditPlaylistDialogue.PlaylistDialogListener {

    boolean inPlaylist = false;

    Toolbar toolBar, toolBar1;
    ImageView favourites, library, playlists;
    NavigationView navigationView;
    DrawerLayout drawerLayout;
    TextView noMusicTextView, songsTV;
    RecyclerView recyclerView, recyclerViewPlaylist;
    ArrayList<AudioModel> songsList = new ArrayList<>();
    ArrayList<AudioModel> favSongsList = new ArrayList<>();
    SongOperations songOperations = new SongOperations(this);
    private String searchText = "";
    private String Artist = "";
    private String Album = "";
    private ViewBy viewBy = ViewBy.SONG;
    private boolean favouriteFlag = false;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //      this.deleteDatabase("DATABASE_NAME");
        recyclerView = findViewById(R.id.recyclerView);
        noMusicTextView = findViewById(R.id.noSongsText);
        songsTV = findViewById(R.id.songsText);
        drawerLayout = findViewById(R.id.drawer);
        favourites = findViewById(R.id.favourite);
        playlists = findViewById(R.id.playlist);
        favourites.setOnClickListener(v -> displayFavourites());
        playlists.setOnClickListener(v -> displayPlaylists());
        library = findViewById(R.id.library);
        library.setOnClickListener(v -> displayLibrary());
        songsTV.setText("Songs");


        toolBar = findViewById(R.id.toolbar);
        toolBar1 = findViewById(R.id.toolbarPlaylist);

        toolBar.setNavigationIcon(R.drawable.baseline_menu_24);

        toolBar1.setNavigationIcon(R.drawable.baseline_add_24);
        toolBar1.setVisibility(View.GONE);
        setSupportActionBar(toolBar1);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        setSupportActionBar(toolBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);


        // toolBar1.setOnClickListener(v -> displayCreatePlayList());
        toolBar1.setNavigationOnClickListener(v -> displayCreatePlayList());
        // setSupportActionBar(toolBar);
        navigationView = findViewById(R.id.nav_view);
        if (!checkPermission()) {
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
        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null);
        while (cursor.moveToNext()) {
            AudioModel song = new AudioModel(cursor.getString(1), cursor.getString(0), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
            if (new File(song.getPath()).exists())
                songsList.add(song);
            if (!songOperations.isSong(song.getTitle())) {
                songOperations.addSong(song);
            }
        }
        cursor.close();
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
            getSongListInActivity(songsList);
        }


        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.open_view_by, R.string.close_view_by);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);


    }

    boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
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
            getSongListInActivity(songsList);
        }
        toolBar.setVisibility(View.VISIBLE);
        toolBar1.setVisibility(View.INVISIBLE);
    }

    private void displayFavourites() {
        inPlaylist = false;

        toolBar.setVisibility(View.VISIBLE);
        toolBar1.setVisibility(View.INVISIBLE);
        FavouriteOperations favouriteOperations = new FavouriteOperations(this);
        favSongsList = favouriteOperations.getAllFavorites();
        viewBy = ViewBy.SONG;
        if (favSongsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            noMusicTextView.setVisibility(View.GONE);

        }
        songsTV.setText("Favourite Songs");
        getSongListInActivity(favSongsList);
    }

    private void displayLibrary() {
        inPlaylist = false;
        toolBar.setVisibility(View.VISIBLE);
        toolBar1.setVisibility(View.INVISIBLE);
        songsTV.setText("Songs");
        if (songsList.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            noMusicTextView.setVisibility(View.GONE);

        }
        getSongListInActivity(songsList);
    }

    private void displayPlaylists() {

        inPlaylist = true;


        toolBar.setVisibility(View.INVISIBLE);
        // toolBar.setNavigationIcon(0);
        toolBar1.setVisibility(View.VISIBLE);
        PlaylistOperations playlistOperations = new PlaylistOperations(this);
        ArrayList<PlaylistModel> playlists = playlistOperations.getAllPlaylists();
        if (playlists.size() == 0) {
            noMusicTextView.setVisibility(View.VISIBLE);
        } else {
            noMusicTextView.setVisibility(View.GONE);

        }
        songsTV.setText("Playlists");
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.BELOW, R.id.toolbarPlaylist);
//        songsTV.setLayoutParams(params);
        getPlaylistInActivity();
    }

    void displayCreatePlayList() {

        PlaylistDialog playlistDialog = new PlaylistDialog();
        playlistDialog.show(getSupportFragmentManager(), "Create playlist dialog");


    }

    public boolean onCreateOptionsMenu(Menu menu) {
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
            String searchString = "";
            switch (viewBy) {
                case SONG:
                    searchString = item.getTitle();
                    break;
                case ALBUM:
                    searchString = item.getAlbum();
                    break;
                case ARTIST:
                    searchString = item.getArtist();
                    break;

            }
            if (searchString.toLowerCase().contains(s)) {
                searchedSongsList.add(item);

            }


        }
        getSongListInActivity(searchedSongsList);

    }

    public void getSongListInActivity(ArrayList<AudioModel> songs) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MusicListAdapter(songs, viewBy, getApplicationContext()));
    }

    public void getPlaylistInActivity() {
        PlaylistOperations playlistOperations = new PlaylistOperations(this);

        ArrayList<PlaylistModel> playlists = playlistOperations.getAllPlaylists();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new PlaylistAdapeter(playlists, this, noMusicTextView));
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
        songsTV.setText("Songs");
        setViewBy();
        return true;
    }

    public void setViewBy() {
        ArrayList<AudioModel> groupedSongs = songOperations.getAllSongsByGroup(viewBy.toString());
        getSongListInActivity(groupedSongs);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void applyText(String playlistName) {
        PlaylistModel playlist = new PlaylistModel(playlistName);
        PlaylistOperations playlistOperations = new PlaylistOperations(this);
        playlistOperations.addPlayList(playlist);
        getPlaylistInActivity();
    }

    @Override
    public void applyEditText(String playlistName, String oldPlaylistName) {
        PlaylistOperations playlistOperations = new PlaylistOperations(this);
        PlaylistModel playlist = playlistOperations.getPlaylist(oldPlaylistName);

        playlistOperations.updatePlaylist(playlist, playlistName);
        getPlaylistInActivity();

    }
}

//TODO: when switching view by method then the first song plays immediately and sometime array index out of bounds exception
// TODO: the progress bar stays at the end
//TODO: when there are too many recyclerviews things get messy with the bar underneath