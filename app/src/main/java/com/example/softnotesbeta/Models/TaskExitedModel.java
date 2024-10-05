package com.example.softnotesbeta.Models;

public class TaskExitedModel {

    public interface OnTaskClosedListener {
        void onTaskClosed();
    }

    private static TaskExitedModel mInstance;
    private OnTaskClosedListener mListener;

    private TaskExitedModel() {

    }

    public static TaskExitedModel getInstance() {
        if (mInstance == null) {
            mInstance = new TaskExitedModel();
        }
        return mInstance;
    }

    public void setListener(OnTaskClosedListener listener) {
        this.mListener = listener;
    }

    public void createTask() {
        if (mListener != null) {
            mListener.onTaskClosed();
        }
    }
}
