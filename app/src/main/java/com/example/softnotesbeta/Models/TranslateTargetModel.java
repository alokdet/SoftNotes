package com.example.softnotesbeta.Models;

public class TranslateTargetModel {

    public interface TargetListener {
        void onTargetChanged(String language);
    }

    private static TranslateTargetModel mInstance;
    private TargetListener mListener;

    private TranslateTargetModel() {

    }

    public static TranslateTargetModel getInstance() {
        if (mInstance == null) {
            mInstance = new TranslateTargetModel();
        }
        return mInstance;
    }

    public void setListener(TargetListener listener) {
        this.mListener = listener;
    }

    public void changeTargetLanguage(String language) {
        if (mListener != null) {
            mListener.onTargetChanged(language);
        }
    }
}
