package com.example.app_manga_g4.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.app_manga_g4.R;
import com.example.app_manga_g4.data.model.Chapter;
import com.example.app_manga_g4.data.model.Comic;
import com.example.app_manga_g4.ui.reader.ReaderActivity;

import java.util.List;

// Activity hiển thị chi tiết thông tin truyện và danh sách các chương thật
public class ComicDetailActivity extends AppCompatActivity {

    public static final String EXTRA_COMIC = "extra_comic";

    private DetailViewModel viewModel;
    private ChapterAdapter adapter;
    private ProgressBar pbLoading;
    private List<Chapter> currentChapterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        Comic comic = (Comic) getIntent().getSerializableExtra(EXTRA_COMIC);
        if (comic == null) {
            finish();
            return;
        }

        initViews(comic);
        initViewModel(comic.getId());
    }

    private void initViews(Comic comic) {
        ImageView imgCover = findViewById(R.id.imgDetailCover);
        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvAuthor = findViewById(R.id.tvDetailAuthor);
        TextView tvStatus = findViewById(R.id.tvDetailStatus);
        TextView tvDesc = findViewById(R.id.tvDetailDesc);
        Button btnReadFirst = findViewById(R.id.btnReadFirst);
        pbLoading = findViewById(R.id.pbDetailLoading);
        RecyclerView rvChapters = findViewById(R.id.rvChapters);

        tvTitle.setText(comic.getTitle());
        tvAuthor.setText("Tác giả: " + comic.getAuthor());
        tvStatus.setText(comic.getStatus());
        tvDesc.setText(comic.getDescription());

        // Glide nạp ảnh bìa thật từ Supabase Storage
        Glide.with(this)
                .load(comic.getCoverUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imgCover);

        // Click vào 1 chapter -> Mở màn hình đọc ReaderActivity
        adapter = new ChapterAdapter(chapter -> {
            openReader(chapter.getId());
        });

        rvChapters.setLayoutManager(new LinearLayoutManager(this));
        rvChapters.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvChapters.setAdapter(adapter);

        // Click nút "Đọc từ đầu" -> Mở chương đầu tiên trong danh sách
        btnReadFirst.setOnClickListener(v -> {
            if (currentChapterList != null && !currentChapterList.isEmpty()) {
                openReader(currentChapterList.get(0).getId());
            } else {
                Toast.makeText(this, "Đang tải danh sách chương...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openReader(int chapterId) {
        Intent intent = new Intent(ComicDetailActivity.this, ReaderActivity.class);
        intent.putExtra(ReaderActivity.EXTRA_CHAPTER_ID, chapterId);
        startActivity(intent);
    }

    private void initViewModel(int comicId) {
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // LẮNG NGHE LIVEDATA DANH SÁCH CHAPTER TỪ SUPABASE:
        viewModel.getChaptersLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    pbLoading.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    pbLoading.setVisibility(View.GONE);
                    currentChapterList = resource.data;
                    adapter.setChapterList(resource.data);
                    break;
                case ERROR:
                    pbLoading.setVisibility(View.GONE);
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        viewModel.loadChapters(comicId);
    }
}
