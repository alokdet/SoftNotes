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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.softnotesbeta.Adapters.NoteAdapter;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.SearchNotesModel;
import com.example.softnotesbeta.ViewModels.NotesVIewModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements NoteItemClickListener, OnSelectionStart {

    private static final int spanCount = 2;
    private static final int spacing = 38;

    private static final boolean includeEdge = true;

    private RecyclerView recyclerView;
    private AppCompatEditText searchInput;
    private AppCompatImageView actionCloseSearch;

    private NotesVIewModel viewModel;
    private NoteAdapter adapter;

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
        setContentView(R.layout.activity_main4);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.notes_list);
        searchInput = (AppCompatEditText) findViewById(R.id.search_input);
        actionCloseSearch = (AppCompatImageView) findViewById(R.id.close_search);

        viewModel = new ViewModelProvider(this).get(NotesVIewModel.class);
        adapter = new NoteAdapter(getApplicationContext(), this, this);

        viewModel.initAllPreviews("");
        viewModel.allPreviewList.observe(this, previews -> {
            adapter.submitList(previews);
        });

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

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
    public void onPreviewCLick(int position, Preview preview, View view1, View view2, boolean isSelected) {
        if (preview.getType().equals("table")) {
            Intent intent = new Intent(getApplicationContext(), TableNoteWorkspace.class);
            intent.putExtra("transitionName1", ViewCompat.getTransitionName(view1));
            intent.putExtra("title", preview.getTitle());
            intent.putExtra("noteId", preview.getNoteId());
            intent.putExtra("previewId", preview.getId());
            intent.putExtra("mode", "updateAndView");
            intent.putExtra("noteType", preview.getType());

            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, view1, ViewCompat.getTransitionName(view1));
            startActivity(intent, activityOptions.toBundle());
        } else if (preview.getType().equals("timeline")) {
            Intent intent = new Intent(getApplicationContext(), TimelineWorkspace.class);
            intent.putExtra("transitionName1", ViewCompat.getTransitionName(view1));
            intent.putExtra("title", preview.getTitle());
            intent.putExtra("noteId", preview.getNoteId());
            intent.putExtra("previewId", preview.getId());
            intent.putExtra("mode", "updateAndView");
            intent.putExtra("noteType", preview.getType());

            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, view1, ViewCompat.getTransitionName(view1));
            startActivity(intent, activityOptions.toBundle());
        }else {
            Intent intent = new Intent(getApplicationContext(), Workspace.class);
            intent.putExtra("transitionName1", ViewCompat.getTransitionName(view1));
            intent.putExtra("title", preview.getTitle());
            intent.putExtra("noteId", preview.getNoteId());
            intent.putExtra("previewId", preview.getId());
            intent.putExtra("mode", "updateAndView");
            intent.putExtra("noteType", preview.getType());

            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(SearchActivity.this, view1, ViewCompat.getTransitionName(view1));
            startActivity(intent, activityOptions.toBundle());
        }
    }

    @Override
    public void performActions(boolean isSelected) {

    }
}