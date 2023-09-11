package com.example.softnotesbeta.Application;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.softnotesbeta.Adapters.StepContentPager;
import com.example.softnotesbeta.Adapters.StepperAdapter;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.Step;

import java.util.ArrayList;
import java.util.List;

public class TaskService extends Service {

    private List<Step> steps = new ArrayList<>();
    private Step currentStep;
    private int position = 0;

    private NotesDatabase database;
    private TaskDao taskDao;
    private Task currentTask;

    private StepperAdapter stepperAdapter;
    private StepContentPager pagerAdapter;

    private final IBinder binder = new TaskBinder();

    public class TaskBinder extends Binder {
        public TaskService getService() {
            return TaskService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setTask(long taskId) {
        if (database == null) {
            database = NotesDatabase.getInstance(getApplicationContext());
            taskDao = database.taskDao();
        }
        currentTask = taskDao.getTask(taskId);
        steps = currentTask.getSteps();
    }

    public void startTask() {
        if (position != 0) {
            position = 0;
        }
        moveToStep(0);
    }

    public List<Step> getStepsList() {
        return steps;
    }

    public Step getStepAt(int position) {
        return steps.get(position);
    }

    public void moveToStep(int position) {
        currentStep = steps.get(position);
        this.position = position;
    }
}
