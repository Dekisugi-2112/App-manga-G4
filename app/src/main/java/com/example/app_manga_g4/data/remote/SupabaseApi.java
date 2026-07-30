package com.example.app_manga_g4.data.remote;

import com.example.app_manga_g4.data.model.Chapter;
import com.example.app_manga_g4.data.model.Comic;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SupabaseApi {

    // Lấy danh sách truyện từ bảng 'comics' trên Supabase PostgREST API
    @GET("rest/v1/comics?select=*")
    Call<List<Comic>> getComics(@Query("order") String order);

    // Tìm kiếm truyện theo tên
    @GET("rest/v1/comics?select=*")
    Call<List<Comic>> searchComics(@Query("title") String titlePattern);

    // Lấy danh sách chapter của 1 bộ truyện
    @GET("rest/v1/chapters?select=*")
    Call<List<Chapter>> getChaptersForComic(@Query("comic_id") String eqComicId, @Query("order") String order);
}
