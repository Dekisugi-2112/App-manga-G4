package com.example.app_manga_g4.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app_manga_g4.data.model.Chapter;
import com.example.app_manga_g4.data.model.Comic;
import com.example.app_manga_g4.data.model.Page;
import com.example.app_manga_g4.data.remote.SupabaseApi;
import com.example.app_manga_g4.data.remote.SupabaseApiClient;
import com.example.app_manga_g4.utils.Constants;
import com.example.app_manga_g4.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Repository quản lý các thao tác lấy dữ liệu truyện, chapter, trang tranh từ CSDL Supabase
public class ComicRepository {

    private SupabaseApi api;

    public ComicRepository() {
        try {
            api = SupabaseApiClient.getClient().create(SupabaseApi.class);
        } catch (Exception e) {
            api = null;
        }
    }

    // Lấy danh sách truyện từ bảng 'comics' trên Supabase
    public void fetchComics(MutableLiveData<Resource<List<Comic>>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (Constants.SUPABASE_URL.contains("your-supabase-project")) {
            liveData.setValue(Resource.success(getDemoComics()));
            return;
        }

        api.getComics("created_at.desc").enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Lỗi tải danh sách truyện từ Supabase", null));
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối: " + t.getMessage(), null));
            }
        });
    }

    // Lấy danh sách các chương của 1 bộ truyện theo comicId từ bảng 'chapters'
    public void getChaptersForComic(int comicId, MutableLiveData<Resource<List<Chapter>>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (Constants.SUPABASE_URL.contains("your-supabase-project")) {
            liveData.setValue(Resource.success(getDemoChapters(comicId)));
            return;
        }

        api.getChaptersForComic("eq." + comicId, "chapter_number.asc").enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Không có chương nào cho bộ truyện này", null));
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi tải chương: " + t.getMessage(), null));
            }
        });
    }

    // Lấy danh sách các trang ảnh của 1 chapter từ bảng 'pages' trên Supabase
    public void getPagesForChapter(int chapterId, MutableLiveData<Resource<List<Page>>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (Constants.SUPABASE_URL.contains("your-supabase-project")) {
            liveData.setValue(Resource.success(getDemoPages(chapterId)));
            return;
        }

        api.getPagesForChapter("eq." + chapterId, "page_number.asc").enqueue(new Callback<List<Page>>() {
            @Override
            public void onResponse(Call<List<Page>> call, Response<List<Page>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Chưa có trang tranh nào cho chapter này", null));
                }
            }

            @Override
            public void onFailure(Call<List<Page>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi nạp trang tranh: " + t.getMessage(), null));
            }
        });
    }

    // Dữ liệu dự phòng nếu chưa cấu hình URL Supabase thực tế
    private List<Comic> getDemoComics() {
        List<Comic> list = new ArrayList<>();
        list.add(new Comic(1, "One Piece (Vua Hải Tặc)", "Eiichiro Oda",
                "Hành trình của Monkey D. Luffy và băng Mũ Rơm đi tìm kho báu huyền thoại One Piece.",
                "https://upload.wikimedia.org/wikipedia/vi/9/90/One_Piece_Volume_61_Cover.jpg", "Ongoing", 152000));
        return list;
    }

    private List<Chapter> getDemoChapters(int comicId) {
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter(1, comicId, 1.0, "Chương 1: Khởi đầu chuyến hành trình", "2026-07-01"));
        return chapters;
    }

    private List<Page> getDemoPages(int chapterId) {
        List<Page> pages = new ArrayList<>();
        pages.add(new Page(1, chapterId, 1, "https://upload.wikimedia.org/wikipedia/vi/9/90/One_Piece_Volume_61_Cover.jpg"));
        return pages;
    }
}
