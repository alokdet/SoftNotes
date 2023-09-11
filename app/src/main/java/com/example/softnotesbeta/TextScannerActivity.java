package com.example.softnotesbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.softnotesbeta.Adapters.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

public class TextScannerActivity extends AppCompatActivity implements ImageItemClickListener {

    private static final int spanCount = 4;
    private static final int spacing = 10;

    private static final boolean includeEdge = true;

    private static final int READ_PERMISSION = 10;
    private AppCompatImageView actionAddImage;
    private RecyclerView imageGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {

            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.DarkTheme_SoftNotesBeta);
                getWindow().setNavigationBarColor(Color.BLACK);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                setTheme(R.style.LightTheme_SoftNotesBeta);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setNavigationBarColor(Color.WHITE);
                break;
        }

        super.onCreate(savedInstanceState);

        Fade fade = new Fade();
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        getWindow().setEnterTransition(fade);
        getWindow().setExitTransition(fade);

        setContentView(R.layout.activity_text_scanner);

        getSupportActionBar().hide();

        actionAddImage = (AppCompatImageView) findViewById(R.id.image_to_scan);
        imageGrid = (RecyclerView) findViewById(R.id.images_grid);

        if (ContextCompat.checkSelfPermission(TextScannerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TextScannerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
        } else {
            loadImages();
        }
    }

    private void loadImages() {
        ImageAdapter adapter = new ImageAdapter(getAllShownImagePaths(), this, getApplicationContext());

        imageGrid.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        imageGrid.setAdapter(adapter);
        imageGrid.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
    }

    private List<String> getAllShownImagePaths() {
        List<String> imageLists = new ArrayList<>();
        Cursor cursor;
        int columnIndexData, columnIndexFolderName;
        String absoluteImagePath = null;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA, MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        cursor = getContentResolver().query(uri, projection, null, null, null);

        columnIndexData = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        columnIndexFolderName = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

        while (cursor.moveToNext()) {
            absoluteImagePath = cursor.getString(columnIndexData);
            imageLists.add(absoluteImagePath);
        }
        return imageLists;
    }

    @Override
    public void onImageClick(String imagePath) {
        /*
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) actionAddImage.getLayoutParams();
        params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
        actionAddImage.setLayoutParams(params);
        Glide.with(getApplicationContext()).load(imagePath).into(actionAddImage);
         */
        Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
        intent.putExtra("imagePath", imagePath);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == READ_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadImages();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}