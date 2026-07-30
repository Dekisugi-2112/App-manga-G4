package com.example.app_manga_g4.ui.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_manga_g4.data.model.Chapter;
import com.example.app_manga_g4.data.repository.ComicRepository;
import com.example.app_manga_g4.utils.Resource;

import java.util.List;

public class DetailViewModel extends ViewModel {

    private final ComicRepository repository;
    private final MutableLiveData<Resource<List<Chapter>>> chaptersLiveData = new MutableLiveData<>();

    public DetailViewModel() {
        repository = new ComicRepository();
    }

    public LiveData<Resource<List<Chapter>>> getChaptersLiveData() {
        return chaptersLiveData;
    }

    public void loadChapters(int comicId) {
        repository.getChaptersForComic(comicId, chaptersLiveData);
    }
}
