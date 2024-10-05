package com.example.softnotesbeta.Models;

import com.example.softnotesbeta.Entities.Task;

public class EditTaskModel {

    private Task task;

    public interface OnTaskEditRequest {
        void onTaskRequest(long taskId);
    }

    private static EditTaskModel mInstance;
    private OnTaskEditRequest mListener;

    private EditTaskModel() {

    }

    public static EditTaskModel getInstance() {
        if (mInstance == null) {
            mInstance = new EditTaskModel();
        }
        return mInstance;
    }

    public void setListener(OnTaskEditRequest listener) {
        this.mListener = listener;
    }

    public void editTask(long taskId) {
        if (mListener != null) {
            mListener.onTaskRequest(taskId);
        }
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return this.task;
    }
}
