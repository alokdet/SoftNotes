package com.example.softnotesbeta;

import static android.Manifest.permission.CAMERA;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.example.softnotesbeta.Adapters.AvailableLanguagesAdapter;
import com.example.softnotesbeta.Application.CenterZoomLayoutManager;
import com.example.softnotesbeta.Application.SnapToBlock;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Models.SaveNoteModel;
import com.example.softnotesbeta.Models.TranslateCallbacks;
import com.example.softnotesbeta.WorkspaceControllers.NoteControllerHandler;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.Date;
import java.util.Locale;

public class Workspace extends AppCompatActivity implements SaveNoteModel.OnSaveRequest, TranslateCallbacks.Expand {

    private ConstraintLayout layout;
    private ConstraintLayout translateLayout;
    private AppCompatImageView translateBtn;
    private AppCompatImageView closeTranslatePanel;
    private BottomSheetBehavior bottomSheetBehavior;
    private RecyclerView languagesRecyclerView;
    private ConstraintLayout actionsContainer;
    private ConstraintLayout layoutTwo;
    private CoordinatorLayout parentLayout;
    private NoteControllerHandler handler;
    private NotesDatabase database;
    private PreviewDao dao;
    private NoteDao noteDao;
    private long noteId;
    private long previewId;
    private String mode;
    private String noteType;

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
        parentLayout = (CoordinatorLayout) findViewById(R.id.parent_layout);
        translateLayout = findViewById(R.id.bottom_sheet_translate);
        bottomSheetBehavior = BottomSheetBehavior.from(translateLayout);
        translateBtn = translateLayout.findViewById(R.id.translate_btn);
        closeTranslatePanel = translateLayout.findViewById(R.id.close_translation_panel);
        languagesRecyclerView = translateLayout.findViewById(R.id.available_languages);

        handler = NoteControllerHandler.getInstance();

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
                handler.initialise(actionsContainer, layoutTwo, layout, "text", Workspace.this);
                if (sharedText != null) {
                    handler.setNoteText(sharedText);
                    handler.setDate(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));
                }
            }
        } else {
            switch (getIntent().getStringExtra("noteType")) {
                case "text":
                    handler.initialise(actionsContainer, layoutTwo, layout, "text", Workspace.this);
                    if (intent.getStringExtra("text") != null) {
                        handler.setNoteText(intent.getStringExtra("text"));
                        handler.setDate(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));
                    }
                    break;
                case "list":
                    actionsContainer.setVisibility(View.VISIBLE);
                    handler.initialise(actionsContainer, layoutTwo, layout, "list", Workspace.this);
                    break;
                case "image":
                    actionsContainer.setVisibility(View.VISIBLE);
                    handler.initialise(actionsContainer, layoutTwo, layout, "image", Workspace.this);
                    break;
            }
        }

        SaveNoteModel.getInstance().setListener(this::onSaveRequest);

        database = NotesDatabase.getInstance(getApplicationContext());
        dao = database.previewDao();
        noteDao = database.noteDao();

        ViewCompat.setTransitionName(parentLayout, getIntent().getStringExtra("transitionName1"));
        noteId = intent.getLongExtra("noteId", 0);
        previewId = intent.getLongExtra("previewId", 0);
        mode = intent.getStringExtra("mode");
        noteType = intent.getStringExtra("noteType");
        SaveNoteModel.mode = intent.getStringExtra("mode");
        SaveNoteModel.getInstance().setId(previewId);
        TranslateCallbacks.getInstance().setListener(this);

        handler.setDate(new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(new Date()));

        if (noteId != 0) {
            handler.setNote(noteDao.getNote(noteId));
        }

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateCallbacks.getInstance().setTranslateText();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        closeTranslatePanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateCallbacks.getInstance().setTranslateText();
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        languagesRecyclerView.setLayoutManager(new CenterZoomLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        languagesRecyclerView.setAdapter(new AvailableLanguagesAdapter(new TextTranslator().getLanguages()));
        //SnapToBlock snapToBlock = new SnapToBlock(1);
       // snapToBlock.attachToRecyclerView(languagesRecyclerView);


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
    public void onExpand() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
}