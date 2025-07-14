package com.example.softnotesbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.splashscreen.SplashScreen;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Fade;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.softnotesbeta.Adapters.FragmentsAdapter;
import com.example.softnotesbeta.Adapters.NoteAdapter;
import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;

import com.example.softnotesbeta.Models.DeleteNotesModel;
import com.example.softnotesbeta.Models.DeleteTasksModel;
import com.example.softnotesbeta.Models.NotifySelectionModel;
import com.example.softnotesbeta.Models.SearchInvokedModel;
import com.example.softnotesbeta.Models.SearchNotesModel;
import com.example.softnotesbeta.Models.SelectBackModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;


import java.util.List;

public class MainActivity extends AppCompatActivity implements NotifySelectionModel.OnSelectionRequest {

    private int currentFragment;
    private FragmentsAdapter fragmentsAdapter;
    private ViewPager pager;
    private ConstraintLayout actionStart;
    private AppCompatImageView actionDelete;
    private AppCompatImageView actionCamera;
    private AppCompatImageView actionSearch;
    private AppCompatImageView textNoteBtn;
    private AppCompatImageView btn;
    private AppCompatImageView taskTab;
    private AppCompatImageView notesTab;
    private AppCompatImageView mapsTab;
    private AppCompatImageView actionSoftAi;
    private AppCompatImageView actionStartImageNote;
    private AppCompatImageView actionStartListNote;
    private AppCompatImageView actionStartTimelineNote;
    private AppCompatTextView emptyTv;
    private AppCompatImageView actionStartTextNote;
    private CollapsingToolbarLayout toolbarLayout;
    private Vibrator vibrator;
    private DeleteNotesModel deleteModel;
    private DeleteTasksModel deleteTasksModel;
    NotesDatabase database;
    NoteDao dao;
    PreviewDao previewDao;
    private AppCompatImageView actionSettings;
    private ParentChangedListener parentChangedListener;

    private GradientDrawable drawable;
    private boolean isCircle = false;
    //List<View> homeViews = new ArrayList<>();
    private boolean isSelected = false;

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
        actionStartImageNote = (AppCompatImageView) findViewById(R.id.new_image_note);
        actionStartListNote = (AppCompatImageView) findViewById(R.id.new_list_note);
        actionStartTextNote = (AppCompatImageView) findViewById(R.id.new_text_note);
        actionStartTimelineNote = (AppCompatImageView) findViewById(R.id.new_timeline_note);
        actionSoftAi = (AppCompatImageView) findViewById(R.id.action_soft_ai);
        //foldersContainer = (ConstraintLayout) findViewById(R.id.folder_layout);
        pager = (ViewPager) findViewById(R.id.fragments_pager);
        actionDelete = (AppCompatImageView) findViewById(R.id.delete_action);
        //actionCamera = (AppCompatImageView) findViewById(R.id.camera_action);
        actionSearch = (AppCompatImageView) findViewById(R.id.search_action);
        textNoteBtn = (AppCompatImageView) findViewById(R.id.new_text_note);
        //actionSettings = (AppCompatImageView) findViewById(R.id.settings);
        taskTab = (AppCompatImageView) findViewById(R.id.tasks_tab);
        notesTab = (AppCompatImageView) findViewById(R.id.notes_tab);
        mapsTab = (AppCompatImageView) findViewById(R.id.mindmaps_tab);
        emptyTv = (AppCompatTextView) findViewById(R.id.empty_database_text);
        //mapsTab = (AppCompatImageView) findViewById(R.id.maps_tab);
        btn = (AppCompatImageView) findViewById(R.id.one);
        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        deleteModel = DeleteNotesModel.getInstance();
        deleteTasksModel = DeleteTasksModel.getInstance();
        deleteModel.initialise(getApplicationContext());

        //viewPager.setAdapter(new ToolbarPagerAdapter());
        //parentChangedListener.onParentChanged(getIntent().getStringExtra("parentFolder"));

        fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager());
        fragmentsAdapter.add(new NotesFragment());
        fragmentsAdapter.add(new TasksFragment());
        fragmentsAdapter.add(new MindMapFragment());
        pager.setAdapter(fragmentsAdapter);
        currentFragment = 0;

        NotifySelectionModel.getInstance().setListener(this::onSelectionRequest);

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
                        expand();
                        break;
                    case 1:
                        toolbarLayout.setTitle("Tasks");
                        notesTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        taskTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.android_green), PorterDuff.Mode.SRC_IN);
                        mapsTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        currentFragment = 1;
                        collapse();
                        break;
                    case 2:
                        toolbarLayout.setTitle("Mind Maps");
                        notesTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        taskTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.onBackground_light), PorterDuff.Mode.SRC_IN);
                        mapsTab.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.android_green), PorterDuff.Mode.SRC_IN);
                        currentFragment = 2;
                        collapse();
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

        drawable = new GradientDrawable();
        drawable.setCornerRadius(75.0f);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#ED6E6E"));
        actionStart.setBackground(drawable);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        actionSoftAi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SoftWorkspace.class);
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        Pair.create(notesTab, "one"),
                        Pair.create(taskTab, "two"),
                        Pair.create(mapsTab, "three"));
                startActivity(intent, options.toBundle());
            }
        });

        notesTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(0, true);
            }
        });

        taskTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(1, true);
            }
        });

        mapsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pager.setCurrentItem(2, true);
            }
        });

//        actionSettings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
//                startActivity(intent);
//            }
//        });

        actionSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 0) {
                    SearchInvokedModel.getInstance().startSearching();
                } else {
                    SearchInvokedModel.getInstance().startTaskSearching();
                }
/*
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
                            if (currentFragment==0) {
                                SearchNotesModel.getInstance().filterText("%" + s.toString() + "%");
                            } else {
                                SearchNotesModel.getInstance().filterTasksText("%" + s.toString() + "%");
                            }
                        }
                    });
                } else {
                    searchBar.clearFocus();
                    searchBar.setVisibility(View.GONE);
                    if (currentFragment == 0) {
                        toolbarLayout.setTitle("Notes");
                    } else {
                        toolbarLayout.setTitle("Tasks");
                    }
                    actionStart.setVisibility(View.VISIBLE);
                }

                 */
            }
        });

        actionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 1) {
                    Intent intent = new Intent(getApplicationContext(), TaskWorkspace.class);
                    intent.putExtra("work", "create");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                    startActivity(intent, activityOptions.toBundle());
                    //CreateTaskModel.getInstance().createTask();
                }
            }
        });

        actionStartTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 0) {
                    Intent intent = new Intent(getApplicationContext(), Workspace.class);
                    intent.putExtra("noteType", "text");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                    startActivity(intent, activityOptions.toBundle());
                } else {
                    Intent intent = new Intent(getApplicationContext(), TaskWorkspace.class);
                    intent.putExtra("work", "create");
                    ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                    startActivity(intent, activityOptions.toBundle());
                }
            }
        });

        actionStartListNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Workspace.class);
                intent.putExtra("noteType", "list");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                startActivity(intent, activityOptions.toBundle());
            }
        });

        actionStartTimelineNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TimelineWorkspace.class);
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                startActivity(intent, activityOptions.toBundle());
            }
        });

        actionStartImageNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(getApplicationContext(), Workspace.class);
                intent.putExtra("noteType", "image");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                startActivity(intent, activityOptions.toBundle());

                 */
                Intent intent = new Intent(getApplicationContext(), TableNoteWorkspace.class);
                intent.putExtra("noteType", "table");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, actionStart, "actionBtn");
                startActivity(intent, activityOptions.toBundle());
            }
        });

//        actionStart.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                if (currentFragment == 0) {
//                    if (isCircle) {
//                        expand();
//                    } else {
//                        collapse();
//                    }
//                    isCircle = !isCircle;
//                }
//                return true;
//            }
//        });

        actionStartTextNote.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isCircle) {
                    expand();
                } else {
                    collapse();
                }
                isCircle = !isCircle;
                return true;
            }
        });

//        actionCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), TextScannerActivity.class);
//                startActivity(intent);
//            }
//        });

        actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFragment == 0) {
                    deleteModel.deleteNotes();
                    actionDelete.setVisibility(View.GONE);
                    toolbarLayout.setTitle("Notes");
                } else {
                    deleteTasksModel.deleteTasks();
                    actionDelete.setVisibility(View.GONE);
                    toolbarLayout.setTitle("Tasks");
                }
            }
        });
    }

    @Override
    public void onSelectionRequest(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            actionDelete.setVisibility(View.VISIBLE);
        } else {
            actionDelete.setVisibility(View.GONE);
        }
    }

    public interface ParentChangedListener {
        void onParentChanged(String parent);
    }

    public void setParentListener(ParentChangedListener parentChangedListener) {
        this.parentChangedListener = parentChangedListener;
    }

    private int dpToPixels(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private void collapse() {
        actionStartImageNote.setVisibility(View.GONE);
        actionStartListNote.setVisibility(View.GONE);
        actionStartTimelineNote.setVisibility(View.GONE);
        ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(drawable, "cornerRadius", 75f, 200.0f);
        ValueAnimator widthAnimation = ValueAnimator.ofInt(actionStart.getMeasuredWidth(), actionStart.getMeasuredHeight());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionStart.getLayoutParams();
                layoutParams.width = val;
                actionStart.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(cornerAnimation, widthAnimation);
        animatorSet.start();
    }

    private void expand() {
        actionStartImageNote.setVisibility(View.VISIBLE);
        actionStartListNote.setVisibility(View.VISIBLE);
        actionStartTimelineNote.setVisibility(View.VISIBLE);
        ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(drawable, "cornerRadius", 200.0f, 75f);
        ValueAnimator widthAnimation = ValueAnimator.ofInt(actionStart.getMeasuredWidth(), (actionStart.getMeasuredHeight() * 4));
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionStart.getLayoutParams();
                layoutParams.width = val;
                actionStart.setLayoutParams(layoutParams);
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(300);
        animatorSet.playTogether(cornerAnimation, widthAnimation);
        animatorSet.start();
    }
    private void vibrate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(200);
        }
    }

    @Override
    public void onBackPressed() {
        if (isSelected) {
            if (currentFragment==0) {
                SelectBackModel.getInstance().backNote();
            } else {
                SelectBackModel.getInstance().backTask();
            }
            isSelected = false;
        } else {
            super.onBackPressed();
            return;
        }
    }
}