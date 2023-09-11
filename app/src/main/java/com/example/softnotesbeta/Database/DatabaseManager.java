package com.example.softnotesbeta.Database;

import android.content.Context;

import androidx.paging.DataSource;

import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;

import java.util.List;

public class DatabaseManager {

    private static DatabaseManager INSTANCE;
    private Context context;
    private NotesDatabase database;
    private NoteDao noteDao;
    private PreviewDao previewDao;

    private DatabaseManager(Context context) {
        this.context = context;
        database = NotesDatabase.getInstance(context);
        noteDao = database.noteDao();
        previewDao = database.previewDao();
    }

    public static DatabaseManager getInstance() {
        return INSTANCE;
    }

    public synchronized static void initialise(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseManager(context);
        }
    }

    public DataSource.Factory<Integer, Preview> getAllPreviews() {
        return previewDao.getPreviewsPagedList();
    }

    public DataSource.Factory<Integer, Preview> getSearchedPreviews(String searchQuery) {
        return previewDao.searchPreviews(searchQuery);
    }

    public Preview getPreview(Long id) {
        return previewDao.getPreview(id);
    }

    public void deletePreviews(List<Long> ids) {
        previewDao.deletePreviews(ids);
    }

    public void insertPreview(Preview preview) {
        previewDao.insertNoteToDatabase(preview);
    }

    public void updatePreview(Preview preview) {
        previewDao.updatePreview(preview);
    }

    public Note getNote(Long id) {
        return noteDao.getNote(id);
    }

    public void deleteNotes(List<Long> ids) {
        noteDao.deleteNotes(ids);
    }

    public long insertNote(Note note) {
        return noteDao.insertNoteToDatabase(note);
    }

    public void updateNote(Note note) {
        noteDao.updateNote(note);
    }
}
