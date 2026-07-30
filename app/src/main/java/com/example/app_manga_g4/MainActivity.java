package com.example.app_manga_g4;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_manga_g4.data.model.Comic;
import com.example.app_manga_g4.ui.detail.ComicDetailActivity;
import com.example.app_manga_g4.ui.home.ComicAdapter;
import com.example.app_manga_g4.ui.home.HomeViewModel;

public class MainActivity extends AppCompatActivity {

    private HomeViewModel viewModel;
    private ComicAdapter adapter;
    private ProgressBar pbLoading;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initViewModel();
        initSearch();
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pbHomeLoading);
        tvEmpty = findViewById(R.id.tvEmpty);
        RecyclerView rvComics = findViewById(R.id.rvComics);

        // Hiển thị danh sách truyện dạng lưới 2 cột (Grid 2 Columns)
        rvComics.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ComicAdapter(comic -> {
            Intent intent = new Intent(MainActivity.this, ComicDetailActivity.class);
            intent.putExtra(ComicDetailActivity.EXTRA_COMIC, comic);
            startActivity(intent);
        });

        rvComics.setAdapter(adapter);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Lắng nghe dữ liệu từ LiveData chuẩn MVVM 100%
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

        // Tải danh sách truyện
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