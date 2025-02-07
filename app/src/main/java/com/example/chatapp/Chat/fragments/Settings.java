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
import com.google.android.material.switchmaterial.SwitchMaterial;

public class Settings extends DialogFragment {
    public interface SettingsListener {
        public void onSettingsApplyClick(DialogFragment dialog, String serverIP, String serverPort, boolean switch_theme);

        public void onSettingsCancelClick(DialogFragment dialog);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.settings_layout, null, false);

        SwitchMaterial switchMaterial = view.findViewById(R.id.material_switch);

        builder.setView(view)
                .setPositiveButton("Apply", ((dialog, which) -> {
                    String serverIP = ((EditText) view.findViewById(R.id.serverIP)).getText().toString();
                    String serverPort = ((EditText) view.findViewById(R.id.serverPort)).getText().toString();
                    boolean isSwitchChecked = switchMaterial.isChecked();
                    listener.onSettingsApplyClick(this, serverIP, serverPort, isSwitchChecked);
                }))
                .setNegativeButton("Cancel", ((dialog, which) -> listener.onSettingsCancelClick(this)));
        return builder.create();
    }
}
