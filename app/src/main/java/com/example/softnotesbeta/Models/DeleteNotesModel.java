package com.example.softnotesbeta.Models;

import android.content.Context;

import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Preview;

import java.util.ArrayList;
import java.util.List;

public class DeleteNotesModel {

    List<Preview> previewList = new ArrayList<>();
    private NotesDatabase database;
    private PreviewDao previewDao;
    private NoteDao noteDao;

    public interface OnDeleteRequest {
        void onDeleteRequest();
    }

    private static DeleteNotesModel mInstance = null;
    private OnDeleteRequest mListener;

    private DeleteNotesModel() {

    }

    public static synchronized DeleteNotesModel getInstance() {
        if (mInstance == null) {
            mInstance = new DeleteNotesModel();
        }
        return mInstance;
    }

    public void initialise(Context context) {
        database = NotesDatabase.getInstance(context);
        previewDao = database.previewDao();
        noteDao = database.noteDao();
    }

    public void setListener(OnDeleteRequest listener) {
        this.mListener = listener;
    }

    public void deleteNotes() {
        if (mListener != null) {
            mListener.onDeleteRequest();
        }
    }

    public void setItemsToDelete(List<Preview> previews) {
        this.previewList = previews;
        delete();
    }

    private void delete() {
        if (previewList.size() != 0) {
            List<Long> noteIds = new ArrayList<>();
            List<Long> previewIds = new ArrayList<>();

            for (int i = 0; i < previewList.size(); i++) {
                noteIds.add(previewList.get(i).getNoteId());
                noteIds.add(previewList.get(i).getId());
            }

            previewDao.deletePreviews(previewIds);
            noteDao.deleteNotes(noteIds);
        }
    }
}
