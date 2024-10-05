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

import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Entities.Task;

import java.util.concurrent.Executors;

public class TasksVIewModel extends AndroidViewModel {

    private NotesDatabase database;
    TaskDao dao;
    public LiveData<PagedList<Task>> taskList;

    public MutableLiveData<String> filterText = new MutableLiveData<>();

    public TasksVIewModel(@NonNull Application application) {
        super(application);

        database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.taskDao();
    }

    public void initAllPreviews(String parent) {
        PagedList.Config config = new PagedList.Config.Builder().setPageSize(10).setEnablePlaceholders(true).build();
        taskList = Transformations.switchMap(filterText, input -> {
            if (input == null || input.equals("") || input.equals("%%")) {
                return new LivePagedListBuilder<>(dao.getTasksPagedList(), config).build();
            } else {
                return new LivePagedListBuilder<>(dao.searchPreviews(input), config).build();
            }
        });
    }

}
