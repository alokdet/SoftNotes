package com.example.softnotesbeta.Models;

public class SelectBackModel {

    public interface TaskBackRequest {
        void onTaskBackRequest();
    }

    public interface NoteBackRequest {
        void onNoteBackRequest();
    }

    private static SelectBackModel mInstance;
    private TaskBackRequest mListener;
    private NoteBackRequest mListener2;

    private SelectBackModel() {

    }

    public static SelectBackModel getInstance() {
        if (mInstance == null) {
            mInstance = new SelectBackModel();
        }
        return mInstance;
    }

    public void setTaskListener(TaskBackRequest listener) {
        this.mListener = listener;
    }

    public void setNoteListener(NoteBackRequest listener) {
        this.mListener2 = listener;
    }

    public void backTask() {
        if (mListener != null) {
            mListener.onTaskBackRequest();
        }
    }

    public void backNote() {
        if (mListener2 != null) {
            mListener2.onNoteBackRequest();
        }
    }
}
