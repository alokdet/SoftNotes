package com.example.softnotesbeta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

public class SettingsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

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

        setContentView(R.layout.activity_settings);

        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.settings_items);
    }
}