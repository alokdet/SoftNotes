package com.example.softnotesbeta;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.softnotesbeta.Adapters.ContentPageAdapter;
import com.example.softnotesbeta.Adapters.StepsListAdapter;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.Step;

import java.util.List;

public class ContentViewer extends AppCompatActivity implements StepCLickListener {

    private RecyclerView recyclerView;
    private ViewPager2 contentPager;
    private NotesDatabase database;
    private TaskDao taskDao;
    private List<Step> steps;
    private ContentPageAdapter pagerAdapter;
    private StepsListAdapter adapter;
    private long taskID;
    private Task task;

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
        setContentView(R.layout.activity_content_viewer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        taskID = getIntent().getLongExtra("taskId", 0);
        database = NotesDatabase.getInstance(getApplicationContext());
        taskDao = database.taskDao();
        task = taskDao.getTask(taskID);
        steps = task.getSteps();

        recyclerView = (RecyclerView) findViewById(R.id.steps);
        contentPager = (ViewPager2) findViewById(R.id.content);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new StepsListAdapter(steps, getApplicationContext(), this);
        adapter.setFragment(StepsListAdapter.FRAGMENT_VIEW_TASK);
        recyclerView.setAdapter(adapter);

        pagerAdapter = new ContentPageAdapter(steps, getApplicationContext());
        contentPager.setAdapter(pagerAdapter);
    }

//    private void expand() {
//        ValueAnimator heightAnimation = ValueAnimator.ofInt(toolsLayout.getMeasuredHeight(), dpToPixels(150));
//        heightAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
//                int val = (Integer) animation.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = toolsLayout.getLayoutParams();
//                layoutParams.height = val;
//                toolsLayout.setLayoutParams(layoutParams);
//            }
//        });
//
//        ValueAnimator widthAnimation = ValueAnimator.ofInt(toolsLayout.getMeasuredWidth(), contentPager.getMeasuredWidth());
//        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
//                int val = (Integer) animation.getAnimatedValue();
//                ViewGroup.LayoutParams layoutParams = toolsLayout.getLayoutParams();
//                layoutParams.width = val;
//                toolsLayout.setLayoutParams(layoutParams);
//            }
//        });
//
//        ValueAnimator marginAnimation = ValueAnimator.ofInt(dpToPixels(12), 0);
//        marginAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
//                int val = (Integer) animation.getAnimatedValue();
//                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) toolsLayout.getLayoutParams();
//                layoutParams.rightMargin = val;
//                layoutParams.bottomMargin = val;
//                toolsLayout.setLayoutParams(layoutParams);
//            }
//        });
//
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(300);
//        animatorSet.playTogether(heightAnimation, widthAnimation, marginAnimation);
//        animatorSet.start();
//    }

    private int dpToPixels(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    @Override
    public void onStepClicked(Step step, View view, int position) {
        contentPager.setCurrentItem(position, true);
    }
}