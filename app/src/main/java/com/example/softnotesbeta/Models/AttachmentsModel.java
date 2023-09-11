package com.example.softnotesbeta.Models;

import android.graphics.Bitmap;

public class AttachmentsModel {

    public interface OnImageImported {
        void onImageImported(Bitmap bitmap);
    }

    public interface OnAttachmentClicked {
        void onClickAttachment(int attachmentCode);
    }

    public interface OnFloatingViewRequest {
        void onFloatingViewRequested();
    }

    private static AttachmentsModel mInstance = null;
    private OnImageImported mListener;
    private OnAttachmentClicked mClickListener;
    private OnFloatingViewRequest mFloatingViewListener;

    private AttachmentsModel() {

    }

    public static synchronized AttachmentsModel getInstance() {
        if (mInstance == null) {
            mInstance = new AttachmentsModel();
        }
        return mInstance;
    }

    public void setListener(OnImageImported listener) {
        this.mListener = listener;
    }

    public void setClickListener(OnAttachmentClicked listener) {
        this.mClickListener = listener;
    }

    public void setFloatingListener(OnFloatingViewRequest listener) {
        this.mFloatingViewListener = listener;
    }

    public void setImage(Bitmap bitmap) {
        if (mListener != null) {
            mListener.onImageImported(bitmap);
        }
    }

    public void actionTriggered(int attachmentCode) {
        if (mClickListener != null) {
            mClickListener.onClickAttachment(attachmentCode);
        }
    }

    public void launchFloatingView() {
        if (mFloatingViewListener != null) {
            mFloatingViewListener.onFloatingViewRequested();
        }
    }
}
