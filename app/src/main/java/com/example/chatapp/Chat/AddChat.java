package com.example.chatapp.Chat;

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

public class AddChat extends DialogFragment {

    public interface AddChatListener{
        public void onAddClick(DialogFragment dialog,String name);
        public void onCloseClick(DialogFragment dialog);
    }

    AddChatListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            listener=(AddChatListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString() + " must implement AddChatListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // builder and inflater from the current activity
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=requireActivity().getLayoutInflater();

        View view=inflater.inflate(R.layout.add_chat_layout,null,false);

        builder.setView(view)
                .setPositiveButton(R.string.addNewUser,((dialog, which) -> {
            listener.onAddClick(this,((EditText)view.findViewById(R.id.addName)).getText().toString());
        }))
                .setNegativeButton(R.string.closeDialog,((dialog, which) -> {
                    listener.onCloseClick(this);
                }));


        return builder.create();
    }
}
