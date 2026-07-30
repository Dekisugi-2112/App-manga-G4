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

// Activity hiển thị trình đọc tranh tự động nạp ảnh từ Supabase Storage
public class ReaderActivity extends AppCompatActivity {

    public static final String EXTRA_COMIC_ID = "extra_comic_id";
    public static final String EXTRA_CHAPTER_ID = "extra_chapter_id";
    public static final String EXTRA_TOTAL_PAGES = "extra_total_pages";

    private ReaderViewModel viewModel;
    private PageAdapter adapter;
    private ProgressBar pbLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        int comicId = getIntent().getIntExtra(EXTRA_COMIC_ID, 1);
        int chapterId = getIntent().getIntExtra(EXTRA_CHAPTER_ID, -1);
        int totalPages = getIntent().getIntExtra(EXTRA_TOTAL_PAGES, 10);

        if (chapterId == -1) {
            finish();
            return;
        }

        initViews();
        initViewModel(comicId, chapterId, totalPages);
    }

    private void initViews() {
        pbLoading = findViewById(R.id.pbReaderLoading);
        RecyclerView rvPages = findViewById(R.id.rvPages);

        rvPages.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PageAdapter();
        rvPages.setAdapter(adapter);
    }

    private void initViewModel(int comicId, int chapterId, int totalPages) {
        viewModel = new ViewModelProvider(this).get(ReaderViewModel.class);

        // LẮNG NGHE LIVEDATA TỰ ĐỘNG NẠP ANH TỪ SUPABASE STORAGE:
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

        // Tự động nạp các trang ảnh từ Storage theo quy tắc đặt tên 1.jpg, 2.jpg, 3.jpg...
        viewModel.loadPagesAutomatically(comicId, chapterId, totalPages);
    }
}
