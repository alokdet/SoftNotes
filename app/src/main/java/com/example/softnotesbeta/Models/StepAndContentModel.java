package com.example.softnotesbeta.Models;

public class StepAndContentModel {

    public interface ContentCreatedListener {
        void onContentCreated(String contentSoftScript, int position);
    }

    private static StepAndContentModel mInstance;
    private ContentCreatedListener mListener;

    private StepAndContentModel() {

    }

    public static StepAndContentModel getInstance() {
        if (mInstance == null) {
            mInstance = new StepAndContentModel();
        }
        return mInstance;
    }

    public void setListener(ContentCreatedListener listener) {
        this.mListener = listener;
    }

    public void notifyContent(String contentSoftScript, int position) {
        if (mListener != null) {
            mListener.onContentCreated(contentSoftScript, position);
        }
    }
}
