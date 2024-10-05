package com.example.softnotesbeta.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.ContentItem;

import java.util.List;

@Dao
public interface ContentDao {

    @Query("SELECT * FROM content WHERE parent LIKE :parent")
    LiveData<List<ContentItem>> getContentItemsList(Long parent);

    @Query("SELECT * FROM content WHERE id = :id")
    ContentItem getItem(Long id);

    @Query("DELETE FROM content WHERE id IN (:contentIds)")
    void deleteItems(List<Long> contentIds);

    @Insert
    void insertItemToDatabase(ContentItem item);

    @Update
    void updatePreview(ContentItem item);
}
