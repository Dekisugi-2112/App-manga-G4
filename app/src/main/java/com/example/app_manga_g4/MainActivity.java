package com.example.app_manga_g4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

// Activity Trang chủ hiển thị danh sách truyện thật từ Supabase CSDL
public class MainActivity extends AppCompatActivity {

    private HomeViewModel viewModel;
    private SessionManager sessionManager;
    private ComicAdapter adapter;
    private ProgressBar pbLoading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(this);
        // Kiểm tra xem đã đăng nhập chưa, nếu chưa -> Chuyển về LoginActivity
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        initViews();
        initViewModel();
        initSearch();
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pbHomeLoading);
        tvEmpty = findViewById(R.id.tvEmpty);
        TextView tvUserEmail = findViewById(R.id.tvUserEmail);
        ImageButton btnLogout = findViewById(R.id.btnLogout);
        RecyclerView rvComics = findViewById(R.id.rvComics);

        // Hiển thị email người dùng đang đăng nhập
        tvUserEmail.setText("Xin chào: " + sessionManager.getUserEmail());

        // Bắt sự kiện Đăng xuất
        btnLogout.setOnClickListener(v -> {
            sessionManager.logout();
            Toast.makeText(this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        // Thiết lập hiển thị danh sách lưới 2 cột
        rvComics.setLayoutManager(new GridLayoutManager(this, 2));

        // Click vào 1 truyện -> Mở màn hình Chi tiết truyện
        adapter = new ComicAdapter(comic -> {
            Intent intent = new Intent(MainActivity.this, ComicDetailActivity.class);
            intent.putExtra(ComicDetailActivity.EXTRA_COMIC, comic);
            startActivity(intent);
        });

        rvComics.setAdapter(adapter);
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

        // Bảo ViewModel nạp danh sách truyện
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