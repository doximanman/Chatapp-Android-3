package com.example.chatapp.Chat.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.chatapp.R;

public class Settings extends DialogFragment {
    public interface SettingsListener {
        public void onSettingsClick(DialogFragment dialog, String serverIP, String serverPort, boolean darkMode);

        public void onCloseClick(DialogFragment dialog);
    }

    SettingsListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (Settings.SettingsListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " must implement SettingsListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // builder and inflater from the current activity
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.settings_layout, null, false);
        builder.setView(view)
                .setPositiveButton("Settings", ((dialog, which) -> {
                    listener.onSettingsClick(this, ((EditText) view.findViewById(R.id.addName)).getText().toString());
                }))
                .setNegativeButton(R.string.closeDialog, ((dialog, which) -> {
                    listener.onCloseClick(this);
                }));
        return builder.create();
    }


}
