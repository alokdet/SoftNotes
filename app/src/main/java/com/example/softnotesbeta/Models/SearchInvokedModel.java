package com.example.softnotesbeta.Models;

public class SearchInvokedModel {

    public interface OnSearchRequest {
        void onSearchRequest();
    }

    public interface OnTaskSearchRequest {
        void onTaskSearchRequest();
    }

    private static SearchInvokedModel mInstance;
    private OnSearchRequest mListener;
    private OnTaskSearchRequest mTaskListener;

    private SearchInvokedModel() {

    }

    public static SearchInvokedModel getInstance() {
        if (mInstance == null) {
            mInstance = new SearchInvokedModel();
        }
        return mInstance;
    }

    public void setListener(OnSearchRequest listener) {
        this.mListener = listener;
    }

    public void setTaskListener(OnTaskSearchRequest listener) {
        this.mTaskListener = listener;
    }

    public void startSearching() {
        if (mListener != null) {
            mListener.onSearchRequest();
        }
    }

    public void startTaskSearching() {
        if (mListener != null) {
            mTaskListener.onTaskSearchRequest();
        }
    }
}
