package com.example.android.attendance.Models;

import okhttp3.MultipartBody;

public class UpdateImageRequest {

    int id;
    String profile_image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }
}
