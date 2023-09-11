package com.example.softnotesbeta.Models;

public class SaveNoteModel {

    public static String mode;
    private Long id;

    public interface OnSaveRequest {
        void onSaveRequest();
    }

    public interface OnBackPressed {
        void onBackPressed(String mode, long id);
    }

    private static SaveNoteModel mInstance = null;
    private OnSaveRequest mListener;
    private OnBackPressed mListener1;

    private SaveNoteModel() {

    }

    public static synchronized SaveNoteModel getInstance() {
        if (mInstance == null) {
            mInstance = new SaveNoteModel();
        }
        return mInstance;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setListener(OnSaveRequest listener) {
        this.mListener = listener;
    }

    public void setBackListener(OnBackPressed listener) {
        this.mListener1 = listener;
    }

    public void createSaveUpdate() {
        if (mListener != null) {
            mListener.onSaveRequest();
        }
    }

    public void backPressed(String mode, long id) {
        if (mListener1 != null) {
            mListener1.onBackPressed(mode, id);
        }
    }
}
