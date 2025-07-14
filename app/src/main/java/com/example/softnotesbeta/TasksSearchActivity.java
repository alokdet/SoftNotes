package com.example.softnotesbeta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.softnotesbeta.Adapters.NoteAdapter;
import com.example.softnotesbeta.Adapters.TaskAdapter;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.ViewModels.NotesVIewModel;
import com.example.softnotesbeta.ViewModels.TasksVIewModel;

public class TasksSearchActivity extends AppCompatActivity implements OnTaskClickListener, TaskSelectionListener {

    private RecyclerView recyclerView;
    private AppCompatEditText searchInput;
    private AppCompatImageView actionCloseSearch;

    private TasksVIewModel viewModel;
    private TaskAdapter adapter;

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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tasks_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.tasks_list);
        searchInput = (AppCompatEditText) findViewById(R.id.search_input);
        actionCloseSearch = (AppCompatImageView) findViewById(R.id.close_search);

        adapter = new TaskAdapter(getApplicationContext(), this, this::onTaskSelected);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TasksVIewModel.class);

        viewModel.initAllPreviews("");
        viewModel.taskList.observe(this, new Observer<PagedList<Task>>() {
            @Override
            public void onChanged(PagedList<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        viewModel.filterText.setValue("");

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                viewModel.filterText.setValue("%" + s.toString() + "%");
            }
        });

        actionCloseSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        searchInput.requestFocus();
    }

    @Override
    public void onTaskClicked(Task task, int position, View itemView, View checkView, boolean isSelected) {
        Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
        intent.putExtra("task_id", task.getId());
        intent.putExtra("transitionName1", ViewCompat.getTransitionName(itemView));
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(TasksSearchActivity.this, itemView, ViewCompat.getTransitionName(itemView));
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onTaskSelected(int position, Task task, View itemView, View checkView, boolean isSelected) {

    }
}