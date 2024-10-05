package com.example.softnotesbeta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Adapters.StepAdapter;
import com.example.softnotesbeta.Adapters.StepsListAdapter;
import com.example.softnotesbeta.DAOs.StepDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.ViewModels.StepsViewModel;
import com.example.softnotesbeta.ViewModels.StepsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity implements StepCLickListener {


    private RecyclerView recyclerView;
    private AppCompatEditText inputTitle;
    private AppCompatImageView actionEditTask;
    private NotesDatabase database;
    private TaskDao taskDao;
    private Task task;
    private List<Step> steps;
    private StepsListAdapter stepAdapter;
    private long currentTaskId;

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

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        this.currentTaskId = getIntent().getLongExtra("task_id", 0);

        database = NotesDatabase.getInstance(getApplicationContext());
        taskDao = database.taskDao();

        task = taskDao.getTask(currentTaskId);
        steps = task.getSteps();

        recyclerView = (RecyclerView) findViewById(R.id.steps);
        inputTitle = (AppCompatEditText) findViewById(R.id.title_bar);
        actionEditTask = (AppCompatImageView) findViewById(R.id.edit_task);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        stepAdapter = new StepsListAdapter(steps, getApplicationContext(), this::onStepClicked);
        stepAdapter.setFragment(StepsListAdapter.FRAGMENT_VIEW_TASK);
        recyclerView.setAdapter(stepAdapter);

        inputTitle.setText(task.getTitle());

        actionEditTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaskWorkspace.class);
                intent.putExtra("work", "update");
                intent.putExtra("task_id", currentTaskId);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStepClicked(Step step, View view, int position) {
        Intent intent2222 = new Intent(getApplicationContext(), ContentViewer.class);
        intent2222.putExtra("taskId", currentTaskId);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity3.this, recyclerView, "steps_shared_list");
        startActivity(intent2222, activityOptions.toBundle());
    }
}