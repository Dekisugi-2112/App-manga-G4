package com.example.app_manga_g4.data.remote;

import com.example.app_manga_g4.data.model.User;
import com.google.gson.annotations.SerializedName;

// DTO nhận kết quả trả về từ Supabase Auth API khi Đăng ký/Đăng nhập thành công
public class AuthResponse {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("token_type")
    private String tokenType;

    @SerializedName("user")
    private User user;

    public String getAccessToken() {
        return accessToken;
    }

    public User getUser() {
        return user;
    }
}
