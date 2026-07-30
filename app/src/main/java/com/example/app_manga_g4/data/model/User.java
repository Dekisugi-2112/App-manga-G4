package com.example.app_manga_g4.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

// DTO chứa thông tin User đăng nhập
public class User implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("email")
    private String email;

    public User() {
    }

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }
}
