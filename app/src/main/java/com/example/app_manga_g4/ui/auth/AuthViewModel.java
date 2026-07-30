package com.example.app_manga_g4.ui.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_manga_g4.data.remote.AuthResponse;
import com.example.app_manga_g4.data.repository.AuthRepository;
import com.example.app_manga_g4.utils.Resource;

// ViewModel quản lý LiveData và logic cho Đăng ký & Đăng nhập
public class AuthViewModel extends ViewModel {

    private final AuthRepository repository;
    private final MutableLiveData<Resource<AuthResponse>> authLiveData = new MutableLiveData<>();

    public AuthViewModel() {
        repository = new AuthRepository();
    }

    // LiveData công khai cho View (Activity) theo dõi
    public LiveData<Resource<AuthResponse>> getAuthLiveData() {
        return authLiveData;
    }

    // Logic Đăng ký tài khoản
    public void signUp(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            authLiveData.setValue(Resource.error("Vui lòng điền đầy đủ Email và Mật khẩu!", null));
            return;
        }
        if (password.length() < 6) {
            authLiveData.setValue(Resource.error("Mật khẩu phải có ít nhất 6 ký tự!", null));
            return;
        }
        repository.signUp(email, password, authLiveData);
    }

    // Logic Đăng nhập tài khoản
    public void login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            authLiveData.setValue(Resource.error("Vui lòng điền đầy đủ Email và Mật khẩu!", null));
            return;
        }
        repository.login(email, password, authLiveData);
    }
}
