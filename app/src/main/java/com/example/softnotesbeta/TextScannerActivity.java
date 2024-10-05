package com.example.softnotesbeta;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.graphics.Bitmap;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TextScannerActivity extends AppCompatActivity implements ImageItemClickListener {

    private static final int spanCount = 4;
    private static final int spacing = 10;

    private static final boolean includeEdge = true;

    private static final int READ_PERMISSION = 10;
    private static final int PERMISSION_CODE = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 300;
    //private boolean imageCaptured = false;
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

        if (ContextCompat.checkSelfPermission(TextScannerActivity.this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(TextScannerActivity.this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, READ_PERMISSION);
        } else {
            loadImages();
        }

        actionAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    captureImage();
                } else {
                    requestPermission();
                }
            }
        });
    }

    private boolean checkPermission() {
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, PERMISSION_CODE);
    }

    private void captureImage() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePicture, REQUEST_IMAGE_CAPTURE);
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
            Uri uri1 = Uri.fromFile(new File(absoluteImagePath));
            imageLists.add(uri1.toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri imageUri = getImageUri(photo);
            Intent intent = new Intent(getApplicationContext(), MainActivity2.class);
            intent.putExtra("imagePath", imageUri.toString());
            startActivity(intent);

        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "TITLE", null);
        return Uri.parse(path);
    }
}