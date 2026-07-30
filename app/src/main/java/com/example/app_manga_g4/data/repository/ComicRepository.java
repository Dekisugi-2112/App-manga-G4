package com.example.app_manga_g4.data.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.app_manga_g4.data.model.Chapter;
import com.example.app_manga_g4.data.model.Comic;
import com.example.app_manga_g4.data.remote.SupabaseApi;
import com.example.app_manga_g4.data.remote.SupabaseApiClient;
import com.example.app_manga_g4.utils.Constants;
import com.example.app_manga_g4.utils.Resource;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComicRepository {

    private SupabaseApi api;

    public ComicRepository() {
        try {
            api = SupabaseApiClient.getClient().create(SupabaseApi.class);
        } catch (Exception e) {
            api = null;
        }
    }

    public void fetchComics(MutableLiveData<Resource<List<Comic>>> liveData) {
        liveData.setValue(Resource.loading(null));

        // Kiểm tra nếu chưa cấu hình URL Supabase thực tế -> Trả dữ liệu Demo giả lập để xem thử UI
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
                    // Fallback demo data khi API chưa có bảng
                    liveData.setValue(Resource.success(getDemoComics()));
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                // Fallback demo data để hiển thị thử nghiệm khi chưa kết nối CSDL
                liveData.setValue(Resource.success(getDemoComics()));
            }
        });
    }

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
                    liveData.setValue(Resource.success(getDemoChapters(comicId)));
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                liveData.setValue(Resource.success(getDemoChapters(comicId)));
            }
        });
    }

    // Dữ liệu mẫu (Demo Mock Data) cho bài tập lớn khi chưa nạp DB Supabase
    private List<Comic> getDemoComics() {
        List<Comic> list = new ArrayList<>();
        list.add(new Comic(1, "One Piece (Vua Hải Tặc)", "Eiichiro Oda",
                "Hành trình của Monkey D. Luffy và băng Mũ Rơm đi tìm kho báu huyền thoại One Piece.",
                "https://upload.wikimedia.org/wikipedia/vi/9/90/One_Piece_Volume_61_Cover.jpg", "Ongoing", 152000));

        list.add(new Comic(2, "Chú Thuật Hồi Chiến (Jujutsu Kaisen)", "Gege Akutami",
                "Itadori Yuji nuốt phải ngón tay của Vua Lời Nguyền Sukuna và bước vào thế giới chú thuật sư.",
                "https://upload.wikimedia.org/wikipedia/vi/3/37/Jujutsu_Kaisen_volume_1_cover.jpg", "Ongoing", 98000));

        list.add(new Comic(3, "Thanh Gươm Diệt Quỷ (Demon Slayer)", "Koyoharu Gotouge",
                "Tanjiro Kamado gia nhập Quân đoàn Diệt Quỷ để cứu em gái Nezuko biến thành quỷ.",
                "https://upload.wikimedia.org/wikipedia/vi/0/09/Demon_Slayer_-_Kimetsu_no_Yaiba_volume_1_cover.jpg", "Completed", 210000));

        list.add(new Comic(4, "Conan (Thám Tử Lừng Danh)", "Gosho Aoyama",
                "Kudo Shinichi bị teo nhỏ thành Conan Edogawa và phá hàng loạt vụ án hóc húa.",
                "https://upload.wikimedia.org/wikipedia/vi/7/77/Detective_Conan_Vol_1.jpg", "Ongoing", 185000));

        return list;
    }

    private List<Chapter> getDemoChapters(int comicId) {
        List<Chapter> chapters = new ArrayList<>();
        chapters.add(new Chapter(101, comicId, 1.0, "Chương 1: Khởi đầu chuyến hành trình", "2026-07-01"));
        chapters.add(new Chapter(102, comicId, 2.0, "Chương 2: Đồng đội đầu tiên", "2026-07-05"));
        chapters.add(new Chapter(103, comicId, 3.0, "Chương 3: Cuộc chiến căng thẳng", "2026-07-12"));
        chapters.add(new Chapter(104, comicId, 4.0, "Chương 4: Bí mật được bật mí", "2026-07-20"));
        return chapters;
    }
}
