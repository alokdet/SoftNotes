package com.example.softnotesbeta.Models;

public class TaskServiceModel {

    public interface TextChangeListener {
        void onFilterText(String input);
    }

    private static TaskServiceModel mInstance;
    private TaskServiceModel.TextChangeListener mListener;

    private TaskServiceModel() {

    }

    public static TaskServiceModel getInstance() {
        if (mInstance == null) {
            mInstance = new TaskServiceModel();
        }
        return mInstance;
    }

    public void setListener(TaskServiceModel.TextChangeListener listener) {
        this.mListener = listener;
    }

    public void filterText(String input) {
        if (mListener != null) {
            mListener.onFilterText(input);
        }
    }
}
