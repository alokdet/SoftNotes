package com.example.softnotesbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.softnotesbeta.Adapters.FragmentsAdapter;
import com.example.softnotesbeta.Adapters.NoteAdapter;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.DeleteNotesModel;
import com.example.softnotesbeta.Models.SearchNotesModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int currentFragment;
    private FragmentsAdapter fragmentsAdapter;
    private ViewPager pager;
    private ConstraintLayout actionStart;
    private ConstraintLayout foldersContainer;
    private AppCompatImageView actionDelete;
    private AppCompatImageView actionCamera;
    private AppCompatImageView actionSearch;
    private AppCompatImageView textNoteBtn;
    private AppCompatImageView taskTab;
    private AppCompatImageView notesTab;
    private AppCompatImageView mapsTab;
    private CollapsingToolbarLayout toolbarLayout;
    private AppCompatEditText searchBar;
    private ViewPager viewPager;
    private Vibrator vibrator;
    NoteAdapter adapter;
    private DeleteNotesModel deleteModel;
    NotesDatabase database;
    NoteDao dao;
    PreviewDao previewDao;
    private AppCompatImageView actionSettings;
    private AppCompatImageView startWorkspaceAction;
    List<Long> selectedNotes;
    //List<View> homeViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

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

        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();

        actionStart = (ConstraintLayout) findViewById(R.id.action_btn);
        //foldersContainer = (ConstraintLayout) findViewById(R.id.folder_layout);
        pager = (ViewPager) findViewById(R.id.fragments_pager);
        actionDelete = (AppCompatImageView) findViewById(R.id.delete_action);
        actionCamera = (AppCompatImageView) findViewById(R.id.camera_action);
        actionSearch = (AppCompatImageView) findViewById(R.id.search_action);
        textNoteBtn = (AppCompatImageView) findViewById(R.id.new_text_note);
        actionSettings = (AppCompatImageView) findViewById(R.id.settings);
        taskTab = (AppCompatImageView) findViewById(R.id.tasks_tab);
        notesTab = (AppCompatImageView) findViewById(R.id.notes_tab);
        mapsTab = (AppCompatImageView) findViewById(R.id.maps_tab);
        startWorkspaceAction = (AppCompatImageView) findViewById(R.id.open_planner);
        searchBar = (AppCompatEditText) findViewById(R.id.search_box);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        viewPager = (ViewPager) findViewById(R.id.pager);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        deleteModel = DeleteNotesModel.getInstance();
        deleteModel.initialise(getApplicationContext());

        //viewPager.setAdapter(new ToolbarPagerAdapter());

        fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager());
        fragmentsAdapter.add(new NotesFragment());
        fragmentsAdapter.add(new TasksFragment());
        fragmentsAdapter.add(new MindMapsFragment());
        pager.setAdapter(fragmentsAdapter);
        currentFragment = 0;

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        toolbarLayout.setTitle("Notes");
                        notesTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.android_green), PorterDuff.Mode.SRC_IN);
                        taskTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        mapsTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        currentFragment = 0;
                        break;
                    case 1:
                        toolbarLayout.setTitle("Tasks");
                        notesTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        taskTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.android_green), PorterDuff.Mode.SRC_IN);
                        mapsTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        currentFragment = 1;
                        break;
                    case 2:
                        toolbarLayout.setTitle("Mind Maps");
                        notesTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        taskTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        mapsTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.android_green), PorterDuff.Mode.SRC_IN);
                        currentFragment = 2;
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        database = NotesDatabase.getInstance(getApplicationContext());
        dao = database.noteDao();
        previewDao = database.previewDao();

        startWorkspaceAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlannerWorkspace.class);
                startActivity(intent);
            }
        });

        actionSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        actionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchBar.getVisibility() == View.GONE) {
                    searchBar.setVisibility(View.VISIBLE);
                    toolbarLayout.setTitle(" ");
                    actionStart.setVisibility(View.GONE);
                    searchBar.requestFocus();
                    //foldersContainer.setVisibility(View.GONE);

                    searchBar.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            SearchNotesModel.getInstance().filterText("%" + s.toString() + "%");
                        }
                    });
                } else {
                    searchBar.clearFocus();
                    searchBar.setVisibility(View.GONE);
                    toolbarLayout.setTitle("Notes");
                    actionStart.setVisibility(View.VISIBLE);
                }
            }
        });

        actionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 0) {
                    Intent intent1 = new Intent(getApplicationContext(), Workspace.class);
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                    startActivity(intent1, activityOptions.toBundle());
                } else {
                    Intent intent = new Intent(getApplicationContext(), TaskWorkspace.class);
                    intent.putExtra("work", "create");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                    startActivity(intent, activityOptions.toBundle());
                    //CreateTaskModel.getInstance().createTask();
                }
            }
        });

        actionCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TextScannerActivity.class);
                startActivity(intent);
            }
        });

        actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteModel.deleteNotes();
            }
        });
    }

    private int dpToPixels(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void expandView(int width, int i) {
        ValueAnimator widthAnimator = ValueAnimator.ofInt(width, i).setDuration(1000);
        widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                actionStart.getLayoutParams().width = value;
                actionStart.requestLayout();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(widthAnimator);
        animatorSet.start();
    }

    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(200);
        }
    }

    class ToolbarPagerAdapter extends PagerAdapter {

        public ToolbarPagerAdapter() {

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            int resId = 0;
            switch (position) {
                case 0:
                    resId = R.id.page_text1;
                    break;
                case 1:
                    resId = R.id.page_text2;
                    break;
                case 2:
                    resId = R.id.page_text3;
                    break;
            }
            return findViewById(resId);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        }
    }
}