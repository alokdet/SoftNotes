package com.example.softnotesbeta.Models;

public class TranslateCallbacks {

    public interface Expand {
        void onExpand();
    }

    public interface TranslateText {
        void onTranslate();
    }

    private static TranslateCallbacks mInstance;
    private Expand mListener;
    private TranslateText translateText;

    private TranslateCallbacks() {

    }

    public static TranslateCallbacks getInstance() {
        if (mInstance == null) {
            mInstance = new TranslateCallbacks();
        }
        return mInstance;
    }

    public void setListener(Expand listener) {
        this.mListener = listener;
    }

    public void setListener(TranslateText listener) {
        this.translateText = listener;
    }

    public void expand() {
        if (mListener != null) {
            mListener.onExpand();
        }
    }

    public void setTranslateText() {
        if (translateText != null) {
            translateText.onTranslate();
        }
    }
}
