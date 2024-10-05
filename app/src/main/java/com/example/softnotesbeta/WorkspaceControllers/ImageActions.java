package com.example.softnotesbeta.WorkspaceControllers;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.R;

public class ImageActions {

    private ConstraintLayout layout;
    private View view;
    private NoteControllerHandler handler;

    public ImageActions(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.image_actions, layout, false);
        handler = NoteControllerHandler.getInstance();
    }

    public void activate() {
        layout.removeAllViews();
        layout.addView(view);

        AppCompatImageView actionInsertDrawing = (AppCompatImageView) view.findViewById(R.id.add_drawing);
        AppCompatImageView actionInsertImage = (AppCompatImageView) view.findViewById(R.id.add_image);
        AppCompatImageView actionOpenCamera = (AppCompatImageView) view.findViewById(R.id.add_image_from_camera);

        actionInsertDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(layout.getContext(), PlannerWorkspace.class);
                //layout.getContext().startActivity(intent);
                handler.sss();
            }
        });

        actionInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        actionOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void deActivate() {
        layout.removeView(view);
    }


    private String getPath(Context applicationContext, Uri imageUri) {
        String result = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = applicationContext.getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null) {
            if (cursor.moveToNext()) {
                int column_index = cursor.getColumnIndexOrThrow(projection[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not Found!";
        }
        return result;
    }
}
