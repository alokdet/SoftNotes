package com.example.softnotesbeta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.softnotesbeta.DAOs.ContentDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.ContentItem;

import java.util.List;

public class ContentViewModel extends AndroidViewModel {

    private NotesDatabase database;
    private ContentDao dao;
    private LiveData<List<ContentItem>> contentListLiveData;
    public ContentViewModel(@NonNull Application application) {
        super(application);

        database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.contentDao();
    }

    public LiveData<List<ContentItem>> getContentListLiveData(Long stepId) {
        return dao.getContentItemsList(stepId);
    }
}
