package com.example.softnotesbeta.ContentControllers.ContentModels;

public class ActionsToActivity {

    public interface ActionRequestImage {
        void onImageCLicked();
    }

    public interface ActionRequestVideo {
        void onVideoCLicked();
    }

    public interface ActionRequestAudio {
        void onAudioCLicked();
    }

    public interface ActionRequestDocument {
        void onDocumentCLicked();
    }

    private static ActionsToActivity mInstance = null;
    private ActionRequestImage imageListener;
    private ActionRequestVideo videoListener;
    private ActionRequestAudio audioListener;
    private ActionRequestDocument documentListener;

    private ActionsToActivity() {

    }

    public static synchronized ActionsToActivity getInstance() {
        if (mInstance == null) {
            mInstance = new ActionsToActivity();
        }
        return mInstance;
    }

    public void setImageListener(ActionRequestImage listener) {
        this.imageListener = listener;
    }

    public void setVideoListener(ActionRequestVideo listener) {
        this.videoListener = listener;
    }

    public void setAudioListener(ActionRequestAudio listener) {
        this.audioListener = listener;
    }

    public void setDocumentListener(ActionRequestDocument listener) {
        this.documentListener = listener;
    }

    public void takeImage() {
        if (imageListener != null) {
            imageListener.onImageCLicked();
        }
    }

    public void takeVideo() {
        if (videoListener != null) {
            videoListener.onVideoCLicked();
        }
    }

    public void takeAudio() {
        if (audioListener != null) {
            audioListener.onAudioCLicked();
        }
    }

    public void takeDocument() {
        if (documentListener != null) {
            documentListener.onDocumentCLicked();
        }
    }
}
