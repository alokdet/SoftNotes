package com.example.softnotesbeta;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.softnotesbeta.Adapters.ContentPageAdapter;
import com.example.softnotesbeta.Adapters.StepperAdapter;
import com.example.softnotesbeta.Adapters.StepsExecuter;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.EditTaskModel;
import com.example.softnotesbeta.Models.Step;

import java.util.ArrayList;
import java.util.List;

public class TaskWorkstation extends Fragment implements StepperItemListener, TextToSpeech.OnInitListener {
    private RecyclerView recyclerView;
    private AppCompatImageView editTask;
    private ViewPager2 viewPager;

    private NotesDatabase database;
    private TaskDao taskDao;
    private Task currentTask;
    private StepperAdapter stepperAdapter;
    private ContentPageAdapter pagerAdapter;
    private StepsExecuter stepsExecuter;
    private long taskId;
    private List<com.example.softnotesbeta.Entities.Step> stepList = new ArrayList<>();
    private TextToSpeech textToSpeech;

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
        //stepList = currentTask.getSteps();
        stepsExecuter = StepsExecuter.getInstance();

        recyclerView = (RecyclerView) view.findViewById(R.id.stepper);
        viewPager = (ViewPager2) view.findViewById(R.id.step_content_pager);
        editTask = (AppCompatImageView) view.findViewById(R.id.edit_task_btn);

        //stepperAdapter = new StepperAdapter(getContext(), this::onStepClicked, stepList);
        //pagerAdapter = new ContentPageAdapter(stepList, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(stepperAdapter);

        viewPager.setAdapter(pagerAdapter);

        textToSpeech = new TextToSpeech(getContext(), this);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                recyclerView.smoothScrollToPosition(position);
                stepperAdapter.moveToStep(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        stepsExecuter.init(stepperAdapter, pagerAdapter);


        editTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stepsExecuter.startTask();
                //textToSpeech.speak(pagerAdapter.getCurrentTextView().getText(), TextToSpeech.QUEUE_FLUSH, null, "Doesn't matter yet");
                EditTaskModel.getInstance().editTask(currentTask.getId());
                EditTaskModel.getInstance().setTask(currentTask);
            }
        });
    }

    @Override
    public void onStepClicked(Step step, int position) {
        viewPager.setCurrentItem(step.getStepIndex() - 1, true);
    }

    @Override
    public void onInit(int status) {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {

            }

            @Override
            public void onError(String utteranceId) {

            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Spannable textHighlight = new SpannableString(pagerAdapter.getCurrentTextView().getText().toString());
                        textHighlight.setSpan(new ForegroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        pagerAdapter.getCurrentTextView().setText(textHighlight);
                    }
                });
            }
        });
    }
}