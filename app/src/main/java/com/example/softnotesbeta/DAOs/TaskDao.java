package com.example.softnotesbeta.DAOs;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Entities.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM tasks")
    DataSource.Factory<Integer, Task> getTasksPagedList();

    @Query("SELECT * FROM tasks WHERE id = :id")
    Task getTask(Long id);

    @Query("DELETE FROM tasks WHERE id IN (:taskIds)")
    void deleteTasks(List<Long> taskIds);

    @Insert
    long insertTaskToDatabase(Task task);

    @Update
    void updateTask(Task task);
}
