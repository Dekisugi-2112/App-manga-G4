package com.example.app_manga_g4.utils;

import android.content.Context;
import android.content.SharedPreferences;

// Class quản lý lưu trữ phiên đăng nhập của người dùng vào SharedPreferences
public class SessionManager {

    private static final String PREF_NAME = "MangaReaderSession";
    private static final String KEY_TOKEN = "access_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private final SharedPreferences pref;
    private final SharedPreferences.Editor editor;

    // Constructor khởi tạo SharedPreferences
    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    // Hàm lưu thông tin đăng nhập khi user đăng nhập thành công
    public void saveAuthSession(String token, String userId, String email) {
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    // Hàm kiểm tra xem người dùng đã đăng nhập hay chưa
    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Lấy Access Token để gắn vào Header API nếu cần
    public String getToken() {
        return pref.getString(KEY_TOKEN, null);
    }

    // Lấy Email người dùng đang đăng nhập
    public String getUserEmail() {
        return pref.getString(KEY_USER_EMAIL, "Khách");
    }

    // Hàm đăng xuất - Xóa sạch thông tin phiên làm việc
    public void logout() {
        editor.clear();
        editor.apply();
    }
}
