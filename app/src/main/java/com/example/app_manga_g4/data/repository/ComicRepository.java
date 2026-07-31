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

// Repository điều phối dữ liệu với cơ chế TỰ ĐỘNG HÓA TẢI TRANG TRANH
public class ComicRepository {

    private SupabaseApi api;

    public ComicRepository() {
        try {
            api = SupabaseApiClient.getClient().create(SupabaseApi.class);
        } catch (Exception e) {
            api = null;
        }
    }

    // Lấy danh sách truyện từ CSDL Supabase
    public void fetchComics(MutableLiveData<Resource<List<Comic>>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (Constants.SUPABASE_URL.contains("your-supabase-project") || api == null) {
            liveData.setValue(Resource.success(getDemoComics()));
            return;
        }

        api.getComics("created_at.desc").enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isEmpty()) {
                        // Nếu mảng rỗng -> Có thể do chưa tắt RLS (Row Level Security) trên Supabase
                        liveData.setValue(Resource.error("Mảng dữ liệu rỗng! Hãy kiểm tra đã tắt RLS trên Supabase chưa.", response.body()));
                    } else {
                        liveData.setValue(Resource.success(response.body()));
                    }
                } else {
                    liveData.setValue(Resource.error("Lỗi HTTP " + response.code() + ": " + response.message(), null));
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi kết nối API: " + t.getMessage(), null));
            }
        });
    }

    // Lấy danh sách các chương của bộ truyện từ bảng 'chapters'
    public void getChaptersForComic(int comicId, MutableLiveData<Resource<List<Chapter>>> liveData) {
        liveData.setValue(Resource.loading(null));

        if (Constants.SUPABASE_URL.contains("your-supabase-project") || api == null) {
            liveData.setValue(Resource.success(getDemoChapters(comicId)));
            return;
        }

        api.getChaptersForComic("eq." + comicId, "chapter_number.asc").enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    liveData.setValue(Resource.success(response.body()));
                } else {
                    liveData.setValue(Resource.error("Chưa có chương nào cho bộ truyện này", null));
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                liveData.setValue(Resource.error("Lỗi tải chương: " + t.getMessage(), null));
            }
        });
    }

    // Tự động sinh danh sách URL trang ảnh từ Storage
    public void generatePagesAutomatically(int comicId, int chapterId, int totalPages, MutableLiveData<Resource<List<Page>>> liveData) {
        liveData.setValue(Resource.loading(null));

        List<Page> pageList = new ArrayList<>();
        int pagesCount = totalPages > 0 ? totalPages : 10;

        String baseUrl = Constants.SUPABASE_URL;
        if (baseUrl != null && !baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        String folderPath = baseUrl + "storage/v1/object/public/chapter-pages/comic_" + comicId + "/chapter_" + chapterId + "/";

        for (int i = 1; i <= pagesCount; i++) {
            String imageUrl = folderPath + i + ".jpg";
            pageList.add(new Page(i, chapterId, i, imageUrl));
        }

        liveData.setValue(Resource.success(pageList));
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
        chapters.add(new Chapter(101, comicId, 1.0, "Chương 1: Khởi đầu chuyến hành trình", 10, "2026-07-01"));
        return chapters;
    }
}
