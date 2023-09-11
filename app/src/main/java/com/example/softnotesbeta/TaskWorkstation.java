package com.example.softnotesbeta;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.softnotesbeta.Adapters.StepContentPager;
import com.example.softnotesbeta.Adapters.StepperAdapter;
import com.example.softnotesbeta.Adapters.StepsExecuter;
import com.example.softnotesbeta.Application.TaskService;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.Step;

import java.util.ArrayList;
import java.util.List;

public class TaskWorkstation extends Fragment implements StepperItemListener {
    private RecyclerView recyclerView;
    private AppCompatImageView actionTask;
    private ViewPager viewPager;

    private NotesDatabase database;
    private TaskDao taskDao;
    private Task currentTask;
    private StepperAdapter stepperAdapter;
    private StepContentPager pagerAdapter;
    private StepsExecuter stepsExecuter;
    private long taskId;
    private List<Step> stepList = new ArrayList<>();

    public TaskWorkstation() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_workstation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskId = requireArguments().getLong("taskId");
        database = NotesDatabase.getInstance(getContext());
        taskDao = database.taskDao();
        currentTask = taskDao.getTask(taskId);
        stepList = currentTask.getSteps();
        stepsExecuter = StepsExecuter.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.stepper);
        viewPager = (ViewPager) view.findViewById(R.id.step_content_pager);
        actionTask = (AppCompatImageView) view.findViewById(R.id.action_task_btn);

        stepperAdapter = new StepperAdapter(getContext(), this::onStepClicked, stepList);
        pagerAdapter = new StepContentPager(getContext(), stepList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(stepperAdapter);

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                recyclerView.smoothScrollToPosition(position);
                stepperAdapter.moveToStep(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        stepsExecuter.init(stepperAdapter, pagerAdapter);


        actionTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepsExecuter.startTask();
            }
        });
    }

    @Override
    public void onStepClicked(Step step, int position) {
        viewPager.setCurrentItem(step.getStepIndex() - 1, true);
    }
}