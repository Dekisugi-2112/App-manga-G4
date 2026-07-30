// ViewModel - Phục vụ
// Nhận yêu cầu từ lễ tân, quản lý danh sách món ăn dưới dạng LiveData, giữ dữ liệu an toàn kể cả khi nhà hàng xoay chuyển.


package com.example.app_manga_g4.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.app_manga_g4.data.model.Comic;
import com.example.app_manga_g4.data.repository.ComicRepository;
import com.example.app_manga_g4.utils.Resource;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final ComicRepository repository;
    private final MutableLiveData<Resource<List<Comic>>> comicsLiveData = new MutableLiveData<>();
    private List<Comic> originalComicList = new ArrayList<>();

    public HomeViewModel() {
        repository = new ComicRepository();
    }

    public LiveData<Resource<List<Comic>>> getComicsLiveData() {
        return comicsLiveData;
    }

    public void loadComics() {
        repository.fetchComics(comicsLiveData);
    }

    // Lọc danh sách truyện theo từ khóa tìm kiếm trên View
    public void searchComics(String query) {
        if (comicsLiveData.getValue() != null && comicsLiveData.getValue().data != null) {
            if (originalComicList.isEmpty()) {
                originalComicList = new ArrayList<>(comicsLiveData.getValue().data);
            }

            if (query == null || query.trim().isEmpty()) {
                comicsLiveData.setValue(Resource.success(originalComicList));
                return;
            }

            List<Comic> filtered = new ArrayList<>();
            for (Comic c : originalComicList) {
                if (c.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    c.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(c);
                }
            }
            comicsLiveData.setValue(Resource.success(filtered));
        }
    }
}
