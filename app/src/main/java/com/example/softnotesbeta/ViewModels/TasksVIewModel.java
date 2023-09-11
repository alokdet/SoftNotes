package com.example.softnotesbeta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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

    TaskDao dao;
    DataSource.Factory<Integer, Task> getAllTasks;
    public LiveData<PagedList<Task>> taskList;

    public TasksVIewModel(@NonNull Application application) {
        super(application);

        NotesDatabase database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.taskDao();

        getAllTasks = dao.getTasksPagedList();

        taskList = new LivePagedListBuilder<>(getAllTasks, 12).setFetchExecutor(Executors.newFixedThreadPool(5)).build();
    }

    public LiveData<PagedList<Task>> getTasksPagedList(){
        return taskList;
    }


}
