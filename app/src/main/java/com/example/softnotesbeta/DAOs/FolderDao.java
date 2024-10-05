package com.example.softnotesbeta.DAOs;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Note;

import java.util.List;

@Dao
public interface FolderDao {

    @Query("SELECT * FROM folders")
    DataSource.Factory<Integer, Folder> getFoldersPagedList();

    @Query("SELECT * FROM folders WHERE id = :id")
    Note getNote(Long id);

    @Query("DELETE FROM folders WHERE id IN (:folderIds)")
    void deleteNotes(List<Long> folderIds);

    @Insert
    long insertFolderDatabase(Folder folder);

    @Update
    void updateFolder(Folder folder);
}
