package com.example.softnotesbeta.Models;

public class CreateTaskModel {

    public interface OnTaskCreateRequest {
        void onTaskRequest();
    }

    private static CreateTaskModel mInstance;
    private OnTaskCreateRequest mListener;

    private CreateTaskModel() {

    }

    public static CreateTaskModel getInstance() {
        if (mInstance == null) {
            mInstance = new CreateTaskModel();
        }
        return mInstance;
    }

    public void setListener(OnTaskCreateRequest listener) {
        this.mListener = listener;
    }

    public void createTask() {
        if (mListener != null) {
            mListener.onTaskRequest();
        }
    }
}
