package com.example.chatapp.database.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User implements Parcelable {
    @NonNull
    @PrimaryKey
    private String username;
    private final String displayName;
    private String profilePic;

    public User(@NonNull String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;

    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfilePic() {
        // sometimes the format starts with "data:image...."
        if (profilePic.startsWith("data"))
            return profilePic.split(";base64,")[1];
        return profilePic;
    }

    public String getUsername() {
        return username;
    }

    public void setProfilePic(String profilePic) {

        this.profilePic = profilePic;

    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeStringArray(new String[]{username, displayName, profilePic});
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        String[] props = in.createStringArray();
        this.username = props[0];
        this.displayName = props[1];
        this.profilePic = props[2];
    }
}