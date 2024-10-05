package com.example.softnotesbeta.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.example.softnotesbeta.DAOs.FolderDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Task;

import java.util.concurrent.Executors;

public class FoldersViewModel extends AndroidViewModel {

    FolderDao dao;
    DataSource.Factory<Integer, Folder> getAllFolders;
    public LiveData<PagedList<Folder>> folderList;

    public FoldersViewModel(@NonNull Application application) {
        super(application);

        NotesDatabase database = NotesDatabase.getInstance(application.getApplicationContext());
        dao = database.folderDao();

        getAllFolders = dao.getFoldersPagedList();

        folderList = new LivePagedListBuilder<>(getAllFolders, 12).setFetchExecutor(Executors.newFixedThreadPool(5)).build();
    }

    public LiveData<PagedList<Folder>> getFoldersPagedList(){
        return folderList;
    }


}
