package com.example.app_manga_g4.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_manga_g4.MainActivity;
import com.example.app_manga_g4.R;
import com.example.app_manga_g4.utils.SessionManager;

// Activity hiển thị giao diện Đăng nhập
public class LoginActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private SessionManager sessionManager;
    private EditText etEmail, etPassword;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        // Nếu đã đăng nhập trước đó -> Chuyển thẳng tới Trang chủ MainActivity
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        initViews();
        initViewModel();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        pbLoading = findViewById(R.id.pbLoginLoading);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvToRegister = findViewById(R.id.tvToRegister);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            viewModel.login(email, password);
        });

        tvToRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // LẮNG NGHE LIVEDATA ĐĂNG NHẬP CHUẨN MVVM:
        viewModel.getAuthLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    pbLoading.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    pbLoading.setVisibility(View.GONE);
                    if (resource.data != null && resource.data.getUser() != null) {
                        // Lưu phiên đăng nhập
                        sessionManager.saveAuthSession(
                                resource.data.getAccessToken(),
                                resource.data.getUser().getId(),
                                resource.data.getUser().getEmail()
                        );
                        Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    break;
                case ERROR:
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
