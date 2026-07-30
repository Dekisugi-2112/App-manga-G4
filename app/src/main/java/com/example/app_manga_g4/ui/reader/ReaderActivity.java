package com.example.app_manga_g4.ui.reader;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_manga_g4.R;

// Activity hiển thị màn hình đọc truyện cuộn trang tranh dọc
public class ReaderActivity extends AppCompatActivity {

    public static final String EXTRA_CHAPTER_ID = "extra_chapter_id";

    private ReaderViewModel viewModel;
    private PageAdapter adapter;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        int chapterId = getIntent().getIntExtra(EXTRA_CHAPTER_ID, -1);
        if (chapterId == -1) {
            finish();
            return;
        }

        initViews();
        initViewModel(chapterId);
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pbReaderLoading);
        RecyclerView rvPages = findViewById(R.id.rvPages);

        // RecyclerView hiển thị danh sách trang ảnh cuộn dọc từ trên xuống dưới
        rvPages.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PageAdapter();
        rvPages.setAdapter(adapter);
    }

    private void initViewModel(int chapterId) {
        viewModel = new ViewModelProvider(this).get(ReaderViewModel.class);

        // LẮNG NGHE LIVEDATA DANH SÁCH THẬT CỦA CÁC TRANG ANH TỪ SUPABASE STORAGE:
        viewModel.getPagesLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    pbLoading.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    pbLoading.setVisibility(View.GONE);
                    adapter.setPageList(resource.data);
                    break;
                case ERROR:
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        // Bảo ViewModel nạp các trang ảnh của chapterId
        viewModel.loadPages(chapterId);
    }
}
