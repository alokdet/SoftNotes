package com.example.softnotesbeta.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Query("SELECT * FROM reminders")
    LiveData<List<Reminder>> getAllReminders();

    @Insert
    void insert(Reminder reminder);

    @Update
    void update(Reminder reminder);
}
