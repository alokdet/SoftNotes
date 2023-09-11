package com.example.softnotesbeta;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MainActivity2 extends AppCompatActivity {

    private String imagePath;
    private AppCompatImageView imageView;
    private AppCompatImageView captureImageBtn;
    private AppCompatImageView detectTextBtn;
    private AppCompatTextView textView;
    private Bitmap imageBitmap;
    private static final int PERMISSION_CODE = 200;
    private static final int REQUEST_IMAGE_CAPTURE = 300;
    private boolean imageCaptured = false;

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


        setContentView(R.layout.activity_main2);

        getSupportActionBar().hide();

        imageView = (AppCompatImageView) findViewById(R.id.view_image);
        textView = (AppCompatTextView) findViewById(R.id.recognised_text);
        captureImageBtn = (AppCompatImageView) findViewById(R.id.capture_image);
        detectTextBtn = (AppCompatImageView) findViewById(R.id.write_text);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");

        Glide.with(getApplicationContext()).load(imagePath).into(imageView);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission()) {
                    captureImage();
                } else {
                    requestPermission();
                }
            }
        });

        detectTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextRecognize textRecognize;
                if (imageBitmap != null) {
                    textRecognize = new TextRecognize(imageBitmap, getApplicationContext());
                    textRecognize.detectText(textView);
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    imageBitmap = BitmapFactory.decodeFile(imagePath, options);
                    imageBitmap = Bitmap.createBitmap(imageBitmap);
                    textRecognize = new TextRecognize(imageBitmap, getApplicationContext());
                    textRecognize.detectText(textView);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
            imageCaptured = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission) {
                captureImage();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}