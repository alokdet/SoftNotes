package com.example.softnotesbeta;

import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;

import com.example.softnotesbeta.Adapters.CreateStepsArch;
import com.example.softnotesbeta.DAOs.ReminderDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Reminder;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.DefineStepModel;

import java.util.Calendar;

public class CreateTaskFragment extends Fragment implements DefineStepModel.OnStepDefineRequest {

    private AppCompatEditText inputTaskTitle;
    private NestedScrollView stepsScroller;
    private LinearLayout stepsContainer;
    private AppCompatTextView scheduleTimeView;
    private AppCompatTextView createdTimeView;
    private AppCompatImageView actionCancelTask;
    private ConstraintLayout actionCreateTask;
    private AppCompatImageView sharedDoneBtn;
    private TaskDao taskDao;
    private ReminderDao reminderDao;
    private NotesDatabase database;
    private CreateStepsArch arch;
    private int hour;
    private int minute;
    private int currentPage;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_task, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = NotesDatabase.getInstance(getContext());
        taskDao = database.taskDao();
        reminderDao = database.reminderDao();

        DefineStepModel.getInstance().setListener(this::onDefineStepRequest);

        inputTaskTitle = (AppCompatEditText) view.findViewById(R.id.task_title);
        stepsScroller = (NestedScrollView) view.findViewById(R.id.steps_list);
        stepsContainer = (LinearLayout) view.findViewById(R.id.step_container);
        actionCancelTask = (AppCompatImageView) view.findViewById(R.id.close_panel);
        actionCreateTask = (ConstraintLayout) view.findViewById(R.id.create_task);
        sharedDoneBtn = (AppCompatImageView) view.findViewById(R.id.action_done_iv);

        arch = CreateStepsArch.getInstance();
        arch.initialize(stepsContainer);

        ViewCompat.setTransitionName(inputTaskTitle, "edittext");
        ViewCompat.setTransitionName(actionCreateTask, "layout");
        ViewCompat.setTransitionName(sharedDoneBtn, "sharedDoneBtn");

        /*
        scheduleTimeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        scheduleTimeView.setText(hourOfDay + ":" + minute);
                        setHour(hourOfDay);
                        setMinute(minute);
                    }
                }, hour, minute, false);
                dialog.show();
            }
        });

         */

        actionCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask(inputTaskTitle.getText().toString());
            }
        });

        actionCancelTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void saveTask(String title) {

        Task task = new Task(title, "Wednesday, May 24", "Friday, May 25");
        task.setSteps(CreateStepsArch.getInstance().getStepList());

        long taskId = taskDao.insertTaskToDatabase(task);

        Reminder reminder = new Reminder(taskId, getHour(), getMinute());
        reminderDao.insert(reminder);
        reminder.schedule(getContext(), title);

        getActivity().finish();
    }

    private void setHour(int hour) {
        this.hour = hour;
    }

    private void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @Override
    public void onDefineStepRequest(int index) {
        //new ContentDialogFragment().show(getChildFragmentManager(), ContentDialogFragment.TAG);
        setSharedElementReturnTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.shared_transition));
        setExitTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));

        Fragment fragment = new StepContentWorkspace();
        fragment.setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(R.transition.shared_transition));
        fragment.setEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.fade));

        getActivity().getSupportFragmentManager().beginTransaction()
                .addSharedElement(inputTaskTitle, "edittext")
                .replace(R.id.fragment_container_view, fragment)
                .addToBackStack(null)
                .commit();
    }
}