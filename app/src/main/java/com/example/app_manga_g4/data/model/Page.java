package com.example.app_manga_g4.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

// DTO chứa thông tin từng trang ảnh đọc truyện (bảng 'pages' trong Supabase)
public class Page implements Serializable {

    @SerializedName("id")
    private int id; // ID trang ảnh

    @SerializedName("chapter_id")
    private int chapterId; // ID của chapter chứa trang ảnh này

    @SerializedName("page_number")
    private int pageNumber; // Số thứ tự trang ảnh (1, 2, 3...)

    @SerializedName("image_url")
    private String imageUrl; // URL đường dẫn ảnh lưu trên Supabase Storage

    public Page() {
    }

    public Page(int id, int chapterId, int pageNumber, String imageUrl) {
        this.id = id;
        this.chapterId = chapterId;
        this.pageNumber = pageNumber;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public int getChapterId() {
        return chapterId;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
