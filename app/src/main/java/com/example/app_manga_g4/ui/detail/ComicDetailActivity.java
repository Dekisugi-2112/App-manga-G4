package com.example.app_manga_g4.ui.detail;

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
import com.example.app_manga_g4.data.model.Comic;

public class ComicDetailActivity extends AppCompatActivity {

    public static final String EXTRA_COMIC = "extra_comic";

    private DetailViewModel viewModel;
    private ChapterAdapter adapter;
    private ProgressBar pbLoading;

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

        Glide.with(this)
                .load(comic.getCoverUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(imgCover);

        adapter = new ChapterAdapter(chapter -> {
            Toast.makeText(ComicDetailActivity.this, "Đang mở: " + chapter.getTitle(), Toast.LENGTH_SHORT).show();
        });

        rvChapters.setLayoutManager(new LinearLayoutManager(this));
        rvChapters.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rvChapters.setAdapter(adapter);

        btnReadFirst.setOnClickListener(v -> {
            if (adapter.getItemCount() > 0) {
                Toast.makeText(ComicDetailActivity.this, "Bắt đầu đọc Chương 1", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViewModel(int comicId) {
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        // Lắng nghe LiveData chuẩn MVVM
        viewModel.getChaptersLiveData().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    pbLoading.setVisibility(View.VISIBLE);
                    break;
                case SUCCESS:
                    pbLoading.setVisibility(View.GONE);
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
