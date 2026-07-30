package com.example.app_manga_g4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_manga_g4.ui.auth.LoginActivity;
import com.example.app_manga_g4.ui.detail.ComicDetailActivity;
import com.example.app_manga_g4.ui.home.ComicAdapter;
import com.example.app_manga_g4.ui.home.HomeViewModel;
import com.example.app_manga_g4.utils.SessionManager;

// Activity Trang chủ hiển thị ngay lập tức khi mở ứng dụng (Không bắt buộc Đăng nhập)
public class MainActivity extends AppCompatActivity {

    private HomeViewModel viewModel;
    private SessionManager sessionManager;
    private ComicAdapter adapter;
    private ProgressBar pbLoading;
    private TextView tvEmpty;
    private TextView tvUserEmail;
    private Button btnAuthAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);

        initViews();
        initViewModel();
        initSearch();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Cập nhật trạng thái người dùng (Khách hoặc Đã đăng nhập) khi quay lại màn hình
        updateUserHeader();
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pbHomeLoading);
        tvEmpty = findViewById(R.id.tvEmpty);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        btnAuthAction = findViewById(R.id.btnAuthAction);
        RecyclerView rvComics = findViewById(R.id.rvComics);

        updateUserHeader();

        // Nút Đăng nhập / Đăng xuất linh hoạt
        btnAuthAction.setOnClickListener(v -> {
            if (sessionManager.isLoggedIn()) {
                sessionManager.logout();
                Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
                updateUserHeader();
            } else {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        // Nạp danh sách lưới 2 cột hiển thị truyện
        rvComics.setLayoutManager(new GridLayoutManager(this, 2));

        // Người dùng (kể cả Khách) đều có thể tự do bấm xem Chi tiết & Đọc truyện
        adapter = new ComicAdapter(comic -> {
            Intent intent = new Intent(MainActivity.this, ComicDetailActivity.class);
            intent.putExtra(ComicDetailActivity.EXTRA_COMIC, comic);
            startActivity(intent);
        });

        rvComics.setAdapter(adapter);
    }

    // Hàm cập nhật hiển thị Header dựa theo trạng thái phiên đăng nhập
    private void updateUserHeader() {
        if (sessionManager.isLoggedIn()) {
            tvUserEmail.setText("Xin chào: " + sessionManager.getUserEmail());
            btnAuthAction.setText("Đăng xuất");
        } else {
            tvUserEmail.setText("Chào mừng Khách");
            btnAuthAction.setText("Đăng nhập");
        }
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // LẮNG NGHE LIVEDATA DANH SÁCH TRUYỆN THẬT TỪ SUPABASE:
        viewModel.getComicsLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    pbLoading.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    pbLoading.setVisibility(View.GONE);
                    if (resource.data != null && !resource.data.isEmpty()) {
                        tvEmpty.setVisibility(View.GONE);
                        adapter.setComicList(resource.data);
                    } else {
                        tvEmpty.setVisibility(View.VISIBLE);
                        adapter.setComicList(null);
                    }
                    break;
                case ERROR:
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        viewModel.loadComics();
    }

    private void initSearch() {
        EditText etSearch = findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.searchComics(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}