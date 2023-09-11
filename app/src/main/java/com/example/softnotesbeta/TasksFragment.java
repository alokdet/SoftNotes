package com.example.softnotesbeta;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.softnotesbeta.ViewModels.TasksVIewModel;


public class TasksFragment extends Fragment implements OnTaskClickListener {

    private RecyclerView recyclerView;
    private ViewGroup parent;
    private TaskDao taskDao;
    private ReminderDao reminderDao;
    private NotesDatabase database;
    private TaskAdapter adapter;
    private TasksVIewModel viewModel;

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

        parent = (ViewGroup) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.tasks_list);

        database = NotesDatabase.getInstance(getContext());
        taskDao = database.taskDao();
        reminderDao = database.reminderDao();

        adapter = new TaskAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(TasksVIewModel.class);
        viewModel.getTasksPagedList().observe(getViewLifecycleOwner(), new Observer<PagedList<Task>>() {
            @Override
            public void onChanged(PagedList<Task> tasks) {
                adapter.submitList(tasks);
            }
        });
    }

    @Override
    public void onTaskClicked(Task task) {
        Intent intent = new Intent(getContext(), TaskWorkspace.class);
        intent.putExtra("work", "view_update");
        intent.putExtra("task_id", task.getId());
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), getActivity().findViewById(R.id.action_btn), "actionBtn");
        startActivity(intent, activityOptions.toBundle());
    }
}