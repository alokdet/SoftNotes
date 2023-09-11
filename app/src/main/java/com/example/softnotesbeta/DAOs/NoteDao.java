package com.example.softnotesbeta.DAOs;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.softnotesbeta.Entities.Note;

import java.util.List;

@Dao
public interface NoteDao {

   // @Query("SELECT * FROM notes")
    //DataSource.Factory<Integer, Note> getNotesPagedList();

    @Query("SELECT * FROM notes WHERE id = :id")
    Note getNote(Long id);

    @Query("DELETE FROM notes WHERE id IN (:noteIds)")
    void deleteNotes(List<Long> noteIds);

    @Insert
    long insertNoteToDatabase(Note note);

    @Update
    void updateNote(Note note);
}
