package com.example.softnotesbeta;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;

import android.content.res.Configuration;
import android.graphics.Bitmap;

import android.graphics.Color;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.softnotesbeta.Models.Dimensions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MainActivity2 extends AppCompatActivity {

    private String imagePath;
    private AppCompatImageView imageView;
    private AppCompatImageView captureImageBtn;
    private AppCompatImageView saveText;
    private AppCompatTextView textView;
    private Bitmap imageBitmap;

    private TextRecognizer textRecognizer;
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
        saveText = (AppCompatImageView) findViewById(R.id.action_finish_task);
        textView = (AppCompatTextView) findViewById(R.id.recognised_text);

        Intent intent = getIntent();
        imagePath = intent.getStringExtra("imagePath");

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        try {
            scaleImageView();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Glide.with(getApplicationContext()).load(imagePath).apply(new RequestOptions().bitmapTransform(new RoundedCornersTransformation(45, 0, RoundedCornersTransformation.CornerType.ALL))).into(imageView);

        recognizeText();

        saveText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Workspace.class);
                intent.putExtra("text", textView.getText().toString());
                intent.putExtra("mode", "create");
                intent.putExtra("noteType", "text");
                startActivity(intent);
            }
        });
    }

    private void scaleImageView() throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(imagePath));

        Dimensions dimensions = new Dimensions(bitmap.getWidth(), bitmap.getHeight());
        Dimensions boundary = new Dimensions(680, 680);

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int boundWidth = boundary.getWidth();
        int boundHeight = boundary.getHeight();
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (originalWidth > boundWidth) {
            newWidth = boundWidth;
            newHeight = (newWidth * originalHeight) / originalWidth;
        }

        if (newHeight > boundHeight) {
            newHeight = boundHeight;
            newWidth = (newHeight * originalWidth) / originalHeight;
        }

        ViewGroup.LayoutParams params = imageView.getLayoutParams();
        params.width = newWidth;
        params.height = newHeight;
        imageView.setLayoutParams(params);
    }

    private void recognizeText() {
        try {
            InputImage inputImage = InputImage.fromFilePath(getApplicationContext(), Uri.parse(imagePath));

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            String recognisedText = text.getText();
                            textView.setText(recognisedText);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}