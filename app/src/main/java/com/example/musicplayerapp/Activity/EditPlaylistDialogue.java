package com.example.musicplayerapp.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.musicplayerapp.R;

public class EditPlaylistDialogue extends AppCompatDialogFragment {


    private EditText editTextPlaylistName;
    private PlaylistDialogListener listener;
    private String oldPlaylistName;

    public EditPlaylistDialogue(String oldPlaylistName) {
        this.oldPlaylistName = oldPlaylistName;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.create_playlist_pop, null);
        builder.setTitle("Edit Playlist").setView(view)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String playlistName = editTextPlaylistName.getText().toString();

                        listener.applyEditText(playlistName, oldPlaylistName);

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        editTextPlaylistName = view.findViewById(R.id.editTextPlaylistName);
        editTextPlaylistName.setText(oldPlaylistName);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PlaylistDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement playlistDialog listener");
        }
    }

    public interface PlaylistDialogListener {

        void applyEditText(String playlistName, String oldPlaylistName);
    }
}
