package com.example.softnotesbeta.Models;

public class DefineStepModel {

    public interface OnStepDefineRequest {
        void onDefineStepRequest(int index);
    }

    private static DefineStepModel mInstance = null;
    private OnStepDefineRequest mListener;

    private DefineStepModel() {

    }

    public static synchronized DefineStepModel getInstance() {
        if (mInstance == null) {
            mInstance = new DefineStepModel();
        }
        return mInstance;
    }

    public void setListener(OnStepDefineRequest listener) {
        this.mListener = listener;
    }

    public void setStepContent(int index) {
        if (mListener != null) {
            mListener.onDefineStepRequest(index);
        }
    }
}
