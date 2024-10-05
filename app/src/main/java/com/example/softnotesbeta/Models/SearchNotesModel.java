package com.example.softnotesbeta.Models;

public class SearchNotesModel {

    public interface TextChangeListener {
        void onFilterText(String input);
    }

    public interface TextChangeListenerForTasks {
        void onFilterTextTasks(String input);
    }

    private static SearchNotesModel mInstance;
    private SearchNotesModel.TextChangeListener mListener;
    private TextChangeListenerForTasks mTasksListener;

    private SearchNotesModel() {

    }

    public static SearchNotesModel getInstance() {
        if (mInstance == null) {
            mInstance = new SearchNotesModel();
        }
        return mInstance;
    }

    public void setListener(SearchNotesModel.TextChangeListener listener) {
        this.mListener = listener;
    }

    public void setTasksListener(SearchNotesModel.TextChangeListenerForTasks listener) {
        this.mTasksListener = listener;
    }

    public void filterText(String input) {
        if (mListener != null) {
            mListener.onFilterText(input);
        }
    }

    public void filterTasksText(String input) {
        if (mTasksListener != null) {
            mTasksListener.onFilterTextTasks(input);
        }
    }
}
