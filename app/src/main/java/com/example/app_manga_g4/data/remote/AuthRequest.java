package com.example.app_manga_g4.data.remote;

import com.google.gson.annotations.SerializedName;

// Body gửi Request cho Supabase Auth GoTrue API
public class AuthRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
