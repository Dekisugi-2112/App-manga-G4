package com.example.app_manga_g4.data.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Chapter implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("comic_id")
    private int comicId;

    @SerializedName("chapter_number")
    private double chapterNumber;

    @SerializedName("title")
    private String title;

    @SerializedName("created_at")
    private String createdAt;

    public Chapter() {
    }

    public Chapter(int id, int comicId, double chapterNumber, String title, String createdAt) {
        this.id = id;
        this.comicId = comicId;
        this.chapterNumber = chapterNumber;
        this.title = title;
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

    public String getCreatedAt() {
        return createdAt;
    }
}
