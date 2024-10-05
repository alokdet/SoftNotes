package com.example.softnotesbeta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

import com.example.softnotesbeta.Application.SoftScript.SoftScriptGenerator;
import com.example.softnotesbeta.ContentControllers.ContentModels.ActionsToActivity;
import com.example.softnotesbeta.Models.EditTaskModel;
import com.example.softnotesbeta.Models.TaskExitedModel;

public class TaskWorkspace extends AppCompatActivity implements EditTaskModel.OnTaskEditRequest {

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
        EditTaskModel.getInstance().setListener(this);

        if (savedInstanceState == null) {
            Bundle bundle = new Bundle();
            bundle.putLong("taskId", getIntent().getLongExtra("task_id", 0));

            if (work.equals("create")) {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, CreateTaskFragment.class, null)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .setReorderingAllowed(true)
                        .add(R.id.fragment_container_view, CreateTaskFragment.class, bundle)
                        .commit();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //TaskExitedModel.getInstance().createTask();
    }

    @Override
    public void onTaskRequest(long taskId) {
        Bundle bundle = new Bundle();
        bundle.putLong("taskId", taskId);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, CreateTaskFragment.class, bundle)
                .commit();
    }
}