package com.example.app_manga_g4.data.remote;

import com.example.app_manga_g4.data.model.Chapter;
import com.example.app_manga_g4.data.model.Comic;
import com.example.app_manga_g4.data.model.Page;
import com.example.app_manga_g4.data.model.UserProfile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

// Interface định nghĩa toàn bộ các API Endpoints gọi tới Supabase
public interface SupabaseApi {

    // === 1. CAC API XAC THUC (SUPABASE AUTH GOTRUE) ===

    // API Đăng ký tài khoản mới trên Supabase Auth
    @POST("auth/v1/signup")
    Call<AuthResponse> signUp(@Body AuthRequest request);

    // API Đăng nhập tài khoản lấy Access Token từ Supabase Auth
    @POST("auth/v1/token?grant_type=password")
    Call<AuthResponse> login(@Body AuthRequest request);


    // === 2. CAC API TRUY VAN CSDL (SUPABASE POSTGREST) ===

    // Thêm profile người dùng vào bảng 'public.profiles'
    @POST("rest/v1/profiles")
    Call<Void> insertProfile(@Body UserProfile profile);

    // Lấy danh sách toàn bộ truyện từ bảng 'comics'
    @GET("rest/v1/comics?select=*")
    Call<List<Comic>> getComics(@Query("order") String order);

    // Lấy danh sách các chương của 1 bộ truyện theo ID từ bảng 'chapters'
    @GET("rest/v1/chapters?select=*")
    Call<List<Chapter>> getChaptersForComic(@Query("comic_id") String eqComicId, @Query("order") String order);

    // Lấy danh sách các trang ảnh của 1 chapter từ bảng 'pages'
    @GET("rest/v1/pages?select=*")
    Call<List<Page>> getPagesForChapter(@Query("chapter_id") String eqChapterId, @Query("order") String order);
}
