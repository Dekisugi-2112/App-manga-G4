package com.example.app_manga_g4.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

// DTO chứa thông tin chương truyện (bảng 'chapters' trên Supabase)
public class Chapter implements Serializable {

    @SerializedName("id")
    private int id; // ID duy nhất của Chapter

    @SerializedName("comic_id")
    private int comicId; // ID của bộ truyện chứa Chapter này

    @SerializedName("chapter_number")
    private double chapterNumber; // Số chương (1.0, 2.0...)

    @SerializedName("title")
    private String title; // Tên chương (VD: Chương 1: Khởi đầu)

    @SerializedName("total_pages")
    private int totalPages; // Tổng số trang ảnh có trong chapter này (Dùng để tự động sinh URL)

    @SerializedName("created_at")
    private String createdAt;

    public Chapter() {
    }

    public Chapter(int id, int comicId, double chapterNumber, String title, int totalPages, String createdAt) {
        this.id = id;
        this.comicId = comicId;
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.totalPages = totalPages > 0 ? totalPages : 10; // Mặc định 10 trang nếu chưa điền
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getComicId() {
        return comicId;
    }

    public double getChapterNumber() {
        return chapterNumber;
    }

    public String getTitle() {
        return title;
    }

    public int getTotalPages() {
        return totalPages > 0 ? totalPages : 10;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
