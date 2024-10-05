package com.example.softnotesbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.view.Window;

import com.example.softnotesbeta.Adapters.FolderAdapter;
import com.example.softnotesbeta.Adapters.TaskReminderCarouselAdapter;
import com.example.softnotesbeta.DAOs.FolderDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Fragments.BottomFragment;
import com.example.softnotesbeta.Fragments.TopFragment;
import com.example.softnotesbeta.Models.TranslateCallbacks;
import com.example.softnotesbeta.ViewModels.FoldersViewModel;
import com.example.softnotesbeta.ViewModels.TasksVIewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

public class LauncherActivity extends AppCompatActivity implements FolderCLickListener {


    private BottomSheetBehavior bottomSheetBehavior;
    private ConstraintLayout createFolderLayout;
    private AppCompatImageView createFolderBtn;
    private AppCompatImageView cancelCreatingFolderBtn;
    private AppCompatImageView configureNewFolder;
    private AppCompatEditText inputFolderName;

    private RecyclerView recyclerView;
    private FolderAdapter folderAdapter;
    private FoldersViewModel foldersViewModel;
    private NotesDatabase database;
    private FolderDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        switch (nightModeFlags) {

            case Configuration.UI_MODE_NIGHT_YES:
                setTheme(R.style.DarkTheme_SoftNotesBeta);
                getWindow().setStatusBarColor(Color.parseColor("#1C1C1C"));
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

        setContentView(R.layout.activity_launcher);

        getSupportActionBar().hide();

        database = NotesDatabase.getInstance(getApplicationContext());
        dao = database.folderDao();

        createFolderLayout = findViewById(R.id.bottom_sheet_folder);
        bottomSheetBehavior = BottomSheetBehavior.from(createFolderLayout);
        inputFolderName = (AppCompatEditText) createFolderLayout.findViewById(R.id.folder_name_et);
        createFolderBtn = (AppCompatImageView) createFolderLayout.findViewById(R.id.finish_folder);
        cancelCreatingFolderBtn = (AppCompatImageView) createFolderLayout.findViewById(R.id.cancel_folder);
        configureNewFolder = (AppCompatImageView) findViewById(R.id.new_folder);
        recyclerView = (RecyclerView) findViewById(R.id.folders_recyclerview);

        folderAdapter = new FolderAdapter(getApplicationContext(), this::onFolderClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(folderAdapter);

        foldersViewModel = new ViewModelProvider(this).get(FoldersViewModel.class);
        foldersViewModel.getFoldersPagedList().observe(this, new Observer<PagedList<Folder>>() {
            @Override
            public void onChanged(PagedList<Folder> folders) {
                folderAdapter.submitList(folders);
            }
        });

        configureNewFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        cancelCreatingFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        createFolderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderName = inputFolderName.getText().toString();
                Folder folder = new Folder(folderName);
                dao.insertFolderDatabase(folder);
            }
        });
    }

    @Override
    public void onFolderClicked(long folderId, String folderName) {
        //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        //intent.putExtra("folderId", folderId);
    }
}