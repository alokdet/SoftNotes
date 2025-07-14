package com.example.softnotesbeta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softnotesbeta.Adapters.TaskAdapter;
import com.example.softnotesbeta.DAOs.ReminderDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.CreateTaskModel;
import com.example.softnotesbeta.Models.DeleteTasksModel;
import com.example.softnotesbeta.Models.NotifySelectionModel;
import com.example.softnotesbeta.Models.SearchInvokedModel;
import com.example.softnotesbeta.Models.SearchNotesModel;
import com.example.softnotesbeta.Models.SelectBackModel;
import com.example.softnotesbeta.ViewModels.TasksVIewModel;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;


public class TasksFragment extends Fragment implements OnTaskClickListener, TaskSelectionListener, DeleteTasksModel.OnDeleteRequest, SearchNotesModel.TextChangeListenerForTasks, SearchInvokedModel.OnTaskSearchRequest, SelectBackModel.TaskBackRequest {

    private RecyclerView recyclerView;
    private ViewGroup parent;
    private TaskDao taskDao;
    private ReminderDao reminderDao;
    private NotesDatabase database;
    private TaskAdapter adapter;
    private TasksVIewModel viewModel;
    private List<Task> selectedTasks;
    private LinearLayoutManager layoutManager;
    private CollapsingToolbarLayout toolbarLayout;
    private boolean selectionMode = false;

    public TasksFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedTasks = new ArrayList<>();

        parent = (ViewGroup) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.tasks_list);
        toolbarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.collapsing_toolbar);

        database = NotesDatabase.getInstance(getContext());
        taskDao = database.taskDao();
        reminderDao = database.reminderDao();

        SearchNotesModel.getInstance().setTasksListener(this::onFilterTextTasks);
        DeleteTasksModel.getInstance().setListener(this::onDeleteRequest);
        SearchInvokedModel.getInstance().setTaskListener(this::onTaskSearchRequest);
        SelectBackModel.getInstance().setTaskListener(this::onTaskBackRequest);

        layoutManager = new LinearLayoutManager(getContext());
        adapter = new TaskAdapter(getContext(), this, this::onTaskSelected);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TasksVIewModel.class);

        viewModel.initAllPreviews("");
        viewModel.taskList.observe(getViewLifecycleOwner(), new Observer<PagedList<Task>>() {
            @Override
            public void onChanged(PagedList<Task> tasks) {
                adapter.submitList(tasks);
            }
        });

        viewModel.filterText.setValue("");
    }

    @Override
    public void onTaskClicked(Task task, int position, View itemView, View checkView, boolean isSelected) {
        if (selectionMode) {
            if (task.isSelected()) {
                //unselect the task
                checkView.setVisibility(View.GONE);
                itemView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.task_preview_background));
                task.setSelected(false);
                selectedTasks.remove(task);
                toolbarLayout.setTitle(selectedTasks.size() + " selected");
                if (selectedTasks.isEmpty()) {
                    selectionMode = false;
                    adapter.setSelection(false);
                    toolbarLayout.setTitle("Tasks");
                    NotifySelectionModel.getInstance().selectChanged(false);
                }
            } else {
                //select the task
                checkView.setVisibility(View.VISIBLE);
                itemView.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.selected_task_preview_background));
                task.setSelected(true);
                selectedTasks.add(task);
                toolbarLayout.setTitle(selectedTasks.size() + " selected");
            }
        } else {
            Intent intent = new Intent(getContext(), MainActivity3.class);
            intent.putExtra("task_id", task.getId());
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), getActivity().findViewById(R.id.action_btn), "actionBtn");
            startActivity(intent, activityOptions.toBundle());
        }
    }

    public void deleteSelectedTasks() {
        List<Long> selectedTasksIDs = new ArrayList<>();
        for (int i = 0; i < selectedTasks.size(); i++) {
            selectedTasksIDs.add(selectedTasks.get(i).getId());
        }
        taskDao.deleteTasks(selectedTasksIDs);
    }

    @Override
    public void onTaskSelected(int position, Task task, View itemView, View checkView, boolean isSelected) {
        selectionMode = isSelected;
        if (isSelected) {
            getActivity().findViewById(R.id.delete_action).setVisibility(View.VISIBLE);
            NotifySelectionModel.getInstance().selectChanged(true);
            toolbarLayout.setTitle(String.valueOf(selectedTasks.size()) + " selected");
        } else {
            getActivity().findViewById(R.id.delete_action).setVisibility(View.GONE);
            NotifySelectionModel.getInstance().selectChanged(false);
            toolbarLayout.setTitle("Tasks");
        }
    }

    @Override
    public void onDeleteRequest() {
        deleteSelectedTasks();
        selectionMode = false;
        adapter.setSelection(false);
        selectedTasks.clear();
        toolbarLayout.setTitle("Tasks");
        getActivity().findViewById(R.id.delete_action).setVisibility(View.GONE);
    }

    @Override
    public void onFilterTextTasks(String input) {
        viewModel.filterText.setValue(input);
    }

    @Override
    public void onTaskSearchRequest() {
        Intent intent = new Intent(getContext(), TasksSearchActivity.class);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), recyclerView, ViewCompat.getTransitionName(recyclerView));
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onTaskBackRequest() {
        recyclerView.setAdapter(null);
        recyclerView.setLayoutManager(null);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        adapter.notifyDataSetChanged();

        toolbarLayout.setTitle("Tasks");
        adapter.setSelection(false);
        selectionMode = false;
        selectedTasks.clear();

        getActivity().findViewById(R.id.delete_action).setVisibility(View.GONE);
    }
}