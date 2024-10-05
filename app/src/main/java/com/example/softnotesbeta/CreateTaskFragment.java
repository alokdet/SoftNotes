package com.example.softnotesbeta;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.softnotesbeta.Adapters.ContentPageAdapter;
import com.example.softnotesbeta.Adapters.FolderAdapter;
import com.example.softnotesbeta.Adapters.StepAdapter;
import com.example.softnotesbeta.Adapters.StepsListAdapter;
import com.example.softnotesbeta.DAOs.ReminderDao;
import com.example.softnotesbeta.DAOs.StepDao;
import com.example.softnotesbeta.DAOs.TaskDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Reminder;
import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.Models.StepAndContentModel;
import com.example.softnotesbeta.Models.TaskExitedModel;
import com.example.softnotesbeta.ViewModels.FoldersViewModel;
import com.example.softnotesbeta.ViewModels.StepsViewModel;
import com.example.softnotesbeta.ViewModels.StepsViewModelFactory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateTaskFragment extends Fragment implements StepAndContentModel.ContentCreatedListener, StepCLickListener, StepDeletedListener, StepUpdateRequest {
    private AppCompatEditText inputStep;
    private AppCompatEditText inputTitle;
    //private AppCompatTextView scheduleTimeView;
    //private AppCompatTextView createdTimeView;
    private ConstraintLayout actionsView;
    private CoordinatorLayout parent;
    private RecyclerView recyclerView;
    private AppCompatImageView actionCancelTask;
    private AppCompatImageView actionCreateTask;
    private AppCompatImageView actionCloseInput;
    private AppCompatImageView sharedDoneBtn;
    private AppCompatImageView timerBtn;
    private AppCompatImageView speechBtn;
    private AppCompatImageView taskSettingsBtn;
    private AppCompatImageView actionUndo;
    private ConstraintLayout actionBtn;
    private AppCompatImageView mainBtn;
    private TaskDao taskDao;
    private StepDao stepDao;
    private Task currentTask;
    private long currentTaskId;
    private ReminderDao reminderDao;
    private NotesDatabase database;
    private List<Step> steps = new ArrayList<>();
    private List<Step> deletedSteps = new ArrayList<>();
    private StepsListAdapter stepAdapter;
    private ContentPageAdapter contentPageAdapter;
    private StepsViewModel viewModel;
    private ViewPager2 viewPager2;
    private int hour;
    private int minute;
    private GradientDrawable drawable;
    private boolean isCircle = true;
    private boolean update = false;
    private int mode = 0;
    private int updatePosition = -1;
    private long timeDelay;
    //private int currentPage;

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
        stepDao = database.stepDao();

        inputStep = (AppCompatEditText) view.findViewById(R.id.step_text);
        inputTitle = (AppCompatEditText) view.findViewById(R.id.input_task_title);
        //mainBtn = (AppCompatImageView) view.findViewById(R.id.main_btn);
        actionCreateTask = (AppCompatImageView) view.findViewById(R.id.add_step);
        taskSettingsBtn = (AppCompatImageView) view.findViewById(R.id.task_settings);
        actionUndo = (AppCompatImageView) view.findViewById(R.id.action_undo);
        timerBtn = (AppCompatImageView) view.findViewById(R.id.task_timer);
        actionCloseInput = (AppCompatImageView) view.findViewById(R.id.close_input);
        //speechBtn = (AppCompatImageView) view.findViewById(R.id.speech);
        parent = (CoordinatorLayout) view.findViewById(R.id.parent_step_workspace);
        recyclerView = (RecyclerView) view.findViewById(R.id.steps_viewer);
        actionBtn = (ConstraintLayout) view.findViewById(R.id.action_btn);


        Bundle bundle = getArguments();
        if (bundle != null) {
            currentTaskId = bundle.getLong("taskId");
            this.mode = 1;
            this.currentTask = taskDao.getTask(currentTaskId);
            setTask(currentTask);
        }

        StepAndContentModel.getInstance().setListener(this);

        drawable = new GradientDrawable();
        drawable.setCornerRadius(75.0f);
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(Color.parseColor("#ED6E6E"));
        actionBtn.setBackground(drawable);
        //ViewCompat.setTransitionName(inputTaskTitle, "edittext");
        //ViewCompat.setTransitionName(actionCreateTask, "layout");
        //ViewCompat.setTransitionName(sharedDoneBtn, "sharedDoneBtn");

        stepAdapter = new StepsListAdapter(steps, getContext(), this);
        stepAdapter.setStepDeletedListener(this::onStepDeleted);
        stepAdapter.setUpdateRequest(this::onUpdateRequested);
        stepAdapter.setFragment(StepsListAdapter.FRAGMENT_CREATE_TASK);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(stepAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCircle) {
                    expand();
                } else {
                    collapse();
                }
            }
        });

        actionUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                undo();
            }
        });

        actionCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCircle) {
                    expand();
                } else {
                    //collapse();
                    if (update) {
                        steps.get(updatePosition).setName(inputStep.getText().toString());
                        stepAdapter.notifyItemChanged(updatePosition);
                        inputStep.setText("");
                        update = false;
                    } else {
                        Step step = new Step(inputStep.getText().toString());
                        step.setStepIndex(steps.size());
                        steps.add(step);
                        stepAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(steps.size() - 1);
                        inputStep.setText("");
                    }
                }
            }
        });

        actionCreateTask.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                collapse();
                return true;
            }
        });

        /*
        mainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Step step = new Step(inputStep.getText().toString());
                steps.add(step);
                stepsListAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(steps.size() - 1);
                inputStep.setText("");
            }
        });

         */
        actionCloseInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputStep.setText("");
                collapse();
            }
        });

        inputStep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (update) {
                        steps.get(updatePosition).setName(inputStep.getText().toString());
                        stepAdapter.notifyItemChanged(updatePosition);
                        inputStep.setText("");
                        update = false;
                    } else {
                        Step step = new Step(inputStep.getText().toString());
                        step.setStepIndex(steps.size() + 1);
                        steps.add(step);
                        stepAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(steps.size() - 1);
                        inputStep.setText("");
                    }
                }
                return true;
            }
        });

        timerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= 33 && !NotificationManagerCompat.from(getContext()).areNotificationsEnabled()) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
                } else {
                    showTimePicker();
                }

            }
        });
    }


    private void setTask(Task task) {
        steps = task.getSteps();
        stepAdapter = new StepsListAdapter(steps, getContext(), this);
        stepAdapter.setStepDeletedListener(this::onStepDeleted);
        stepAdapter.setUpdateRequest(this::onUpdateRequested);
        stepAdapter.setFragment(StepsListAdapter.FRAGMENT_CREATE_TASK);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(stepAdapter);
        inputTitle.setText(task.getTitle());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void saveTask(String title) {

        Task task = new Task(title, "Wednesday, May 24", "Friday, May 25");
        task.setSteps(steps);
        long taskId = taskDao.insertTaskToDatabase(task);

        Reminder reminder = new Reminder(taskId, getHour(), getMinute());
        reminderDao.insert(reminder);
        reminder.schedule(getContext(), title);
    }

    private void updateTask(String title) {

        currentTask.setSteps(steps);
        currentTask.setTitle(title);
        taskDao.updateTask(currentTask);

        Reminder reminder = new Reminder(currentTask.getId(), getHour(), getMinute());
        reminderDao.update(reminder);
        reminder.schedule(getContext(), title);
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

    private int dpToPixels(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    private int pixelsToDp(int pixels) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) pixels / density);
    }

    private int getScreenWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private void collapse() {
        inputStep.setVisibility(View.GONE);
        actionCloseInput.setVisibility(View.GONE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        //int colorFrom = getResources().getColor(R.color.android_green);
        //int colorTo = Color.parseColor("#000000");
        //Drawable drawable1 = actionBtn.getBackground();
        //ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(drawable, "cornerRadius", 75f, 200.0f);

        actionCreateTask.animate()
                .translationX(0)
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500);

        ValueAnimator widthAnimation = ValueAnimator.ofInt(actionBtn.getMeasuredWidth(), actionBtn.getMeasuredHeight());
        //ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorTo, colorFrom);
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                int val = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = actionBtn.getLayoutParams();
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) actionCreateTask.getLayoutParams();
                layoutParams.width = val;
                //params.leftMargin = 0;
                actionBtn.setLayoutParams(layoutParams);
                actionCreateTask.setLayoutParams(params);
            }
        });

        /*
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                DrawableCompat.setTint(drawable1, (int) animation.getAnimatedValue());
            }
        });

         */

//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet.setDuration(300);
//        animatorSet.playTogether(cornerAnimation, widthAnimation, colorAnimation);
//        animatorSet.start();

        widthAnimation.setDuration(500);
        widthAnimation.start();

        //actionCreateTask.setBackground(null);
        timerBtn.setVisibility(View.VISIBLE);
        taskSettingsBtn.setVisibility(View.VISIBLE);
        isCircle = true;
    }

    private void expand() {
        timerBtn.setVisibility(View.GONE);
        taskSettingsBtn.setVisibility(View.GONE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        ValueAnimator animator = ValueAnimator.ofInt(actionBtn.getMeasuredWidth(), (width-dpToPixels(48)));
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {
                        int val = (Integer) valueAnimator.getAnimatedValue();
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) actionBtn.getLayoutParams();
                        params.width = val;
                        actionBtn.setLayoutParams(params);
                    }
                });
        animator.setDuration(500);
        animator.start();


        actionCreateTask.animate()
                .translationX((float) ((width / 2) - (dpToPixels(86))))
                .setInterpolator(new AccelerateInterpolator())
                .setDuration(500);


        actionCloseInput.setVisibility(View.VISIBLE);
        inputStep.setVisibility(View.VISIBLE);

        inputStep.requestFocus();
        isCircle = false;
    }

    ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            stepAdapter.removeItem(position);
        }
    };

    @Override
    public void onContentCreated(String contentSoftScript, int position) {
        steps.get(position).setContentSoftScript(contentSoftScript);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mode == 0) {
            saveTask(inputTitle.getText().toString());
        } else {
            updateTask(inputTitle.getText().toString());
        }
    }

    @Override
    public void onStepClicked(Step step, View view, int position) {
        Intent intent = new Intent(getContext(), ContentActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("title", step.getName());
        intent.putExtra("content", step.getContentSoftScript());
        intent.putExtra("transitionName", ViewCompat.getTransitionName(view));
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, ViewCompat.getTransitionName(view));
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onStepDeleted(int position) {
        this.deletedSteps.add(steps.get(position));
        steps.remove(position);
        stepAdapter.notifyItemRemoved(position);
    }

    private void undo() {
        int stepIndex = deletedSteps.get(deletedSteps.size() - 1).getStepIndex();
        Step step = deletedSteps.get(deletedSteps.size() - 1);
        steps.add(stepIndex, step);
        stepAdapter.notifyItemInserted(deletedSteps.size() - 1);
        deletedSteps.remove(step);
    }

    @Override
    public void onUpdateRequested(int position, String text) {
        update = true;
        if (isCircle) {
            expand();
        }
        inputStep.setText(text);
        this.updatePosition = position;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showTimePicker();
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                setTimeDelay(((calendar.getTimeInMillis()/1000L)-(Calendar.getInstance().getTimeInMillis()/1000L)));
            }
        }, hour, minute, false);

        dialog.show();
//        if (0 <1){
//            Toast.makeText(getContext(), "Can't set reminders for past", Toast.LENGTH_SHORT).show();
//        } else {
//            createWorkRequest();
//        }

        createWorkRequest();

    }

    private void createWorkRequest() {
        Data inputData = new Data.Builder()
                .putString("Title", "Reminder")
                .putString("Message", String.valueOf(getTimeDelay()))
                .build();

        androidx.work.WorkRequest request = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(getTimeDelay(), TimeUnit.SECONDS)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(getContext()).enqueue(request);

    }

    private void setTimeDelay(long timeDelay) {
        this.timeDelay = timeDelay;
    }

    public long getTimeDelay() {
        return this.timeDelay;
    }
}