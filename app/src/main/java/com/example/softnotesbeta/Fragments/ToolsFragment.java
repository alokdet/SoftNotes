package com.example.softnotesbeta.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softnotesbeta.Adapters.FolderAdapter;
import com.example.softnotesbeta.Adapters.TaskAdapter;
import com.example.softnotesbeta.Adapters.TaskReminderCarouselAdapter;
import com.example.softnotesbeta.DAOs.FolderDao;
import com.example.softnotesbeta.DAOs.ReminderDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.FolderCLickListener;
import com.example.softnotesbeta.GridSpacingItemDecoration;
import com.example.softnotesbeta.OnTaskClickListener;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.ViewModels.FoldersViewModel;
import com.example.softnotesbeta.ViewModels.TasksVIewModel;

public class ToolsFragment extends Fragment implements OnTaskClickListener, FolderCLickListener {

    private static final int spanCount = 2;
    private static final int spacing = 38;

    private static final boolean includeEdge = true;
    private RecyclerView tasksReminderCarousel;
    private RecyclerView foldersList;
    private AppCompatImageView navigationBtn;

    private TaskDao taskDao;
    private FolderDao folderDao;
    private NotesDatabase database;
    private TaskReminderCarouselAdapter adapter;
    private FolderAdapter folderAdapter;
    private TasksVIewModel viewModel;
    private FoldersViewModel foldersViewModel;

    public ToolsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tools, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tasksReminderCarousel = (RecyclerView) view.findViewById(R.id.tasks_reminder);
        foldersList = (RecyclerView) view.findViewById(R.id.root_folders);
        navigationBtn = (AppCompatImageView) view.findViewById(R.id.navigate_to_main_folder);

        database = NotesDatabase.getInstance(getContext());
        taskDao = database.taskDao();
        folderDao = database.folderDao();

        adapter = new TaskReminderCarouselAdapter(getContext(), this);
        tasksReminderCarousel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        tasksReminderCarousel.setAdapter(adapter);

        folderAdapter = new FolderAdapter(getContext(), this::onFolderClicked);
        foldersList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        foldersList.setAdapter(folderAdapter);
        foldersList.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));

        viewModel = new ViewModelProvider(this).get(TasksVIewModel.class);
        viewModel.initAllPreviews("");
        viewModel.taskList.observe(getViewLifecycleOwner(), new Observer<PagedList<Task>>() {
            @Override
            public void onChanged(PagedList<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        foldersViewModel = new ViewModelProvider(this).get(FoldersViewModel.class);
        foldersViewModel.getFoldersPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<Folder>>() {
            @Override
            public void onChanged(PagedList<Folder> folders) {
                folderAdapter.submitList(folders);
            }
        });
    }

    @Override
    public void onTaskClicked(Task task, int position, View itemView, View checkView, boolean isSelected) {

    }

    @Override
    public void onFolderClicked(long folderId, String folderName) {

    }
}