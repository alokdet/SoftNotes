package com.example.softnotesbeta;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

import com.example.softnotesbeta.ContentControllers.ContentModels.ActionsToActivity;

public class TaskWorkspace extends AppCompatActivity implements ActionsToActivity.ActionRequestImage, ActionsToActivity.ActionRequestVideo, ActionsToActivity.ActionRequestDocument, ActionsToActivity.ActionRequestAudio {

    private static final int ACTION_PICK_IMAGE = 0;
    private static final int ACTION_PICK_VIDEO = 1;
    private static final int ACTION_PICK_AUDIO = 2;
    private static final int ACTION_PICK_DOCUMENT = 3;
    private String work;

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

        setContentView(R.layout.activity_task_workspace);

        getSupportActionBar().hide();

        work = getIntent().getStringExtra("work");

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putLong("taskId", getIntent().getLongExtra("task_id", 0));

            if (work.equals("create")) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, CreateTaskFragment.class, null)
                        .commit();
            } else if(work.equals("view_update")) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, TaskWorkstation.class, bundle)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, CreateTaskFragment.class, null)
                        .commit();
            }
        }

        ActionsToActivity.getInstance().setImageListener(this);
        ActionsToActivity.getInstance().setVideoListener(this);
        ActionsToActivity.getInstance().setAudioListener(this);
        ActionsToActivity.getInstance().setDocumentListener(this);
    }


    @Override
    public void onImageCLicked() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), ACTION_PICK_IMAGE);
    }

    @Override
    public void onVideoCLicked() {

    }

    @Override
    public void onAudioCLicked() {

    }

    @Override
    public void onDocumentCLicked() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_PICK_IMAGE) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_view, FilePreviewFragment.class, null)
                    .commit();
        }
    }
}