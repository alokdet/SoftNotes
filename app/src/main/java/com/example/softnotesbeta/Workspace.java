package com.example.softnotesbeta;

import static android.Manifest.permission.CAMERA;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.style.ImageSpan;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.softnotesbeta.Application.Common;
import com.example.softnotesbeta.Application.NotesService;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.DatabaseManager;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.AttachmentsModel;
import com.example.softnotesbeta.Models.SaveNoteModel;
import com.example.softnotesbeta.WorkspaceControllers.NoteControllerHandler;

import java.util.Date;
import java.util.Locale;

public class Workspace extends AppCompatActivity implements SaveNoteModel.OnSaveRequest, AttachmentsModel.OnAttachmentClicked, AttachmentsModel.OnFloatingViewRequest {

    private static final int PERMISSION_CODE = 300;
    private static final int REQUEST_IMAGE_CAPTURE = 400;
    private Bitmap imageBitmap;
    private ConstraintLayout layout;
    private AlertDialog dialog;
    private ConstraintLayout actionsContainer;
    private ConstraintLayout layoutTwo;
    private ConstraintLayout parentLayout;
    private NoteControllerHandler handler;
    private NotesDatabase database;
    private PreviewDao dao;
    private NoteDao noteDao;
    private long noteId;
    private long previewId;
    private String mode;

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

        setContentView(R.layout.activity_workspace);

        getSupportActionBar().hide();

        layout = (ConstraintLayout) findViewById(R.id.action_btn);
        layoutTwo = (ConstraintLayout) findViewById(R.id.layout_two);
        actionsContainer = (ConstraintLayout) findViewById(R.id.actions_container);
        parentLayout = (ConstraintLayout) findViewById(R.id.parent_layout);

        if (isNotesServiceRunning()) {
            stopService(new Intent(getApplicationContext(), NotesService.class));
        }

        handler = NoteControllerHandler.getInstance();
        handler.initialise(actionsContainer, layoutTwo, layout);

        SaveNoteModel.getInstance().setListener(this::onSaveRequest);
        AttachmentsModel.getInstance().setClickListener(this::onClickAttachment);
        AttachmentsModel.getInstance().setFloatingListener(this::onFloatingViewRequested);

        database = NotesDatabase.getInstance(getApplicationContext());
        dao = database.previewDao();
        noteDao = database.noteDao();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        parentLayout.setTransitionName(intent.getStringExtra("transitionName1"));
        noteId = intent.getLongExtra("noteId", 0);
        previewId = intent.getLongExtra("previewId", 0);
        mode = intent.getStringExtra("mode");
        SaveNoteModel.mode = intent.getStringExtra("mode");
        SaveNoteModel.getInstance().setId(previewId);

        handler.setDate(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                if (sharedText != null) {
                    handler.setNoteText(sharedText);
                    Common.text = sharedText;
                    handler.setDate(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));
                }
            }
        }

        if (noteId != 0) {
            handler.setNote(noteDao.getNote(noteId));
        }
    }

    private boolean isNotesServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (NotesService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
    public void onBackPressed() {
        SaveNoteModel.getInstance().backPressed(SaveNoteModel.mode, previewId);
    }

    @Override
    public void onSaveRequest() {
        finish();
    }

    @Override
    public void onClickAttachment(int attachmentCode) {
        if (checkPermission()) {
            captureImage();
        } else {
            requestPermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            AttachmentsModel.getInstance().setImage(imageBitmap);
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

    @Override
    public void onFloatingViewRequested() {
        if (checkOverlayDisplayPermission()) {
            startService(new Intent(getApplicationContext(), NotesService.class));
            finish();
        } else {
            requestOverlayDisplayPermission();
        }
    }

    private void requestOverlayDisplayPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package: " + getPackageName()));
        startActivityForResult(intent, RESULT_OK);
    }

    private boolean checkOverlayDisplayPermission() {
        if (!Settings.canDrawOverlays(getApplicationContext())) {
            return false;
        } else {
            return true;
        }
    }
}