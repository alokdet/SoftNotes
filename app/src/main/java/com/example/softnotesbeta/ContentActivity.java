package com.example.softnotesbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.core.view.ViewCompat;
import android.content.res.Configuration;

import android.graphics.Color;

import android.os.Bundle;

import android.transition.Fade;

import android.view.View;
import android.view.Window;
import com.example.softnotesbeta.Models.StepAndContentModel;

public class ContentActivity extends AppCompatActivity {
    private AppCompatEditText stepTitleView;
    private ConstraintLayout rootTransitionView;
    private AppCompatEditText stepContentView;
    private AppCompatImageView actionCloseContentPanel;
    private int currentPosition;

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

        setContentView(R.layout.activity_content);

        getSupportActionBar().hide();

        stepTitleView = (AppCompatEditText) findViewById(R.id.step_title);
        stepContentView = (AppCompatEditText) findViewById(R.id.input_content);
        actionCloseContentPanel = (AppCompatImageView) findViewById(R.id.close_content_panel);
        rootTransitionView = (ConstraintLayout) findViewById(R.id.transition_container);

        ViewCompat.setTransitionName(rootTransitionView, getIntent().getStringExtra("transitionName"));
        currentPosition = getIntent().getIntExtra("position", -1);
        stepTitleView.setText(getIntent().getStringExtra("title"));
        stepContentView.setText(getIntent().getStringExtra("content"));

        actionCloseContentPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();

        StepAndContentModel.getInstance().notifyContent(stepContentView.getText().toString(), currentPosition);
    }
}