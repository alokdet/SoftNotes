package com.example.softnotesbeta.Models;

public class DeleteTasksModel {

    public interface OnDeleteRequest {
        void onDeleteRequest();
    }

    private static DeleteTasksModel mInstance;
    private OnDeleteRequest mListener;

    private DeleteTasksModel() {

    }

    public static DeleteTasksModel getInstance() {
        if (mInstance == null) {
            mInstance = new DeleteTasksModel();
        }
        return mInstance;
    }

    public void setListener(OnDeleteRequest listener) {
        this.mListener = listener;
    }

    public void deleteTasks() {
        if (mListener != null) {
            mListener.onDeleteRequest();
        }
    }
}
