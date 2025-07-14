package com.example.softnotesbeta.Models;

public class NotifySelectionModel {

    public interface OnSelectionRequest {
        void onSelectionRequest(boolean isSelected);
    }

    private static NotifySelectionModel mInstance;
    private OnSelectionRequest mListener;

    private NotifySelectionModel() {

    }

    public static NotifySelectionModel getInstance() {
        if (mInstance == null) {
            mInstance = new NotifySelectionModel();
        }
        return mInstance;
    }

    public void setListener(OnSelectionRequest listener) {
        this.mListener = listener;
    }

    public void selectChanged(boolean isSelected) {
        if (mListener != null) {
            mListener.onSelectionRequest(isSelected);
        }
    }
}
