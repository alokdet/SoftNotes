package com.example.softnotesbeta.DAOs;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.ContentItem;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Step;

import java.util.List;

@Dao
public interface StepDao {

    // WHERE id = :taskId
    @Query("SELECT * FROM steps")
    DataSource.Factory<Integer, Step> getStepsPagedList();

    @Query("SELECT * FROM steps WHERE parentID = :taskId")
    DataSource.Factory<Integer, Step> getStepsPagedListFromId(long taskId);

    @Query("SELECT * FROM steps")
    LiveData<List<Step>> getStepsList();

    @Query("SELECT * FROM steps")
    List<Step> getSteps();

    @Query("SELECT * FROM steps WHERE id = :id")
    Step getItem(Long id);

    @Query("DELETE FROM content WHERE id IN (:contentIds)")
    void deleteItems(List<Long> contentIds);

    @Insert
    long insertItemToDatabase(Step item);

    @Update
    void updatePreview(ContentItem item);
}
