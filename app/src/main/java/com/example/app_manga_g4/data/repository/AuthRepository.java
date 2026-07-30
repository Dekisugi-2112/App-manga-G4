package com.example.app_manga_g4.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app_manga_g4.data.remote.AuthRequest;
import com.example.app_manga_g4.data.remote.AuthResponse;
import com.example.app_manga_g4.data.remote.SupabaseApi;
import com.example.app_manga_g4.data.remote.SupabaseApiClient;
import com.example.app_manga_g4.utils.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Repository xử lý các thao tác Đăng nhập & Đăng ký người dùng với Supabase Auth GoTrue API
public class AuthRepository {

    private SupabaseApi api;

    public AuthRepository() {
        try {
            api = SupabaseApiClient.getClient().create(SupabaseApi.class);
        } catch (Exception e) {
            api = null;
        }
    }

    // Hàm gọi API Đăng ký tài khoản trên Supabase Auth
    public void signUp(String email, String password, MutableLiveData<Resource<AuthResponse>> liveData) {
        liveData.setValue(Resource.loading(null));

        AuthRequest request = new AuthRequest(email, password);
        api.signUp(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Đăng ký thất bại. Email có thể đã tồn tại hoặc mật khẩu chưa đủ 6 ký tự.", null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối API: " + t.getMessage(), null));
            }
        });
    }

    // Hàm gọi API Đăng nhập tài khoản lấy Token từ Supabase Auth
    public void login(String email, String password, MutableLiveData<Resource<AuthResponse>> liveData) {
        liveData.setValue(Resource.loading(null));

        AuthRequest request = new AuthRequest(email, password);
        api.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Email hoặc mật khẩu không chính xác!", null));
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối API: " + t.getMessage(), null));
            }
        });
    }
}
