package com.example.app_manga_g4.ui.reader;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_manga_g4.data.model.Page;
import com.example.app_manga_g4.data.repository.ComicRepository;
import com.example.app_manga_g4.utils.Resource;

import java.util.List;

// ViewModel quản lý LiveData dữ liệu trang ảnh cho màn hình đọc truyện
public class ReaderViewModel extends ViewModel {

    private final ComicRepository repository;
    private final MutableLiveData<Resource<List<Page>>> pagesLiveData = new MutableLiveData<>();

    public ReaderViewModel() {
        repository = new ComicRepository();
    }

    public LiveData<Resource<List<Page>>> getPagesLiveData() {
        return pagesLiveData;
    }

    public void loadPages(int chapterId) {
        repository.getPagesForChapter(chapterId, pagesLiveData);
    }
}
