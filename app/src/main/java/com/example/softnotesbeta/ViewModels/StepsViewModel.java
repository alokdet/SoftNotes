package com.example.softnotesbeta.ViewModels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.softnotesbeta.DAOs.FolderDao;
import com.example.softnotesbeta.DAOs.StepDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Step;
import com.example.softnotesbeta.Entities.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class StepsViewModel extends AndroidViewModel {

    StepDao dao;
    DataSource.Factory<Integer, Step> getAllSteps;
    public LiveData<PagedList<Step>> stepsList;

    public StepsViewModel(@NonNull Application application, long id) {
        super(application);

        NotesDatabase database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.stepDao();

        getAllSteps = dao.getStepsPagedListFromId(id);

        stepsList = new LivePagedListBuilder<>(getAllSteps, 12).setFetchExecutor(Executors.newFixedThreadPool(5)).build();
    }

    public LiveData<PagedList<Step>> getStepsPagedList() {
        return stepsList;
    }
}
