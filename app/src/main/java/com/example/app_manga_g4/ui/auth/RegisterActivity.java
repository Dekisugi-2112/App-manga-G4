package com.example.app_manga_g4.ui.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.app_manga_g4.R;

// Activity xử lý Đăng ký tài khoản người dùng
public class RegisterActivity extends AppCompatActivity {

    private AuthViewModel viewModel;
    private EditText etEmail, etPassword;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        initViewModel();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etRegisterEmail);
        etPassword = findViewById(R.id.etRegisterPassword);
        pbLoading = findViewById(R.id.pbRegisterLoading);
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvToLogin = findViewById(R.id.tvToLogin);

        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            viewModel.signUp(email, password);
        });

        tvToLogin.setOnClickListener(v -> finish());
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        // Lắng nghe LiveData Đăng ký:
        viewModel.getAuthLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    pbLoading.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, "Đăng ký thành công! Vui lòng Đăng nhập.", Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case ERROR:
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
}
