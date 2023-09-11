package com.example.softnotesbeta.DAOs;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;

import java.util.List;

@Dao
public interface PreviewDao {

    @Query("SELECT * FROM previews")
    DataSource.Factory<Integer, Preview> getPreviewsPagedList();

    @Query("SELECT * FROM previews WHERE title LIKE :search " + "OR text LIKE :search " + "OR date LIKE :search")
    DataSource.Factory<Integer, Preview> searchPreviews(String search);

    @Query("SELECT * FROM previews WHERE id = :id")
    int isNoteExist(long id);

    @Query("SELECT * FROM previews WHERE id = :id")
    Preview getPreview(Long id);

    @Query("DELETE FROM previews WHERE id IN (:previewIds)")
    void deletePreviews(List<Long> previewIds);

    @Insert
    void insertNoteToDatabase(Preview preview);

    @Update
    void updatePreview(Preview preview);
}
