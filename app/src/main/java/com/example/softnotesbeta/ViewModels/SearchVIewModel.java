package com.example.softnotesbeta.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Preview;

import java.util.concurrent.Executors;

public class SearchVIewModel extends AndroidViewModel {

    private PreviewDao dao;

    public LiveData<PagedList<Preview>> allPreviewList;
    public MutableLiveData<String> filterText = new MutableLiveData<>();

    public SearchVIewModel(@NonNull Application application) {
        super(application);

        NotesDatabase database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.previewDao();
    }

    public void initAllPreviews(String parent) {
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).build();
        allPreviewList = Transformations.switchMap(filterText, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
                return new LivePagedListBuilder<>(dao.getAllPreviewsPagedList(), config).build();
            } else {
                return new LivePagedListBuilder<>(dao.searchPreviews(input), config).build();
            }
        });
    }
}
