package com.example.softnotesbeta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.DatabaseManager;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;

import java.util.concurrent.Executors;

public class NotesVIewModel extends AndroidViewModel {

    private NotesDatabase database;
    private PreviewDao dao;
    public LiveData<PagedList<Preview>> allPreviewList;
    public MutableLiveData<String> filterText = new MutableLiveData<>();

    public NotesVIewModel(@NonNull Application application) {
        super(application);

        database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.previewDao();
    }

    public void initAllPreviews(String parent) {
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(true).build();
        allPreviewList = Transformations.switchMap(filterText, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
                return new LivePagedListBuilder<>(dao.getAllPreviewsPagedList(), config).build();
            } else {
                return new LivePagedListBuilder<>(dao.searchPreviews(input), config).build();
            }
        });
    }
}
