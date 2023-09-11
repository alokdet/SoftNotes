package com.example.softnotesbeta.Adapters;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.TypeConverters;

import com.example.softnotesbeta.Application.SoftScript.SoftScriptGenerator;
import com.example.softnotesbeta.Application.SoftScript.SoftScriptReader;
import com.example.softnotesbeta.Application.TaskService;
import com.example.softnotesbeta.Models.SampleModel;
import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.SampleListener;
import com.example.softnotesbeta.StepperItemListener;

import java.util.List;

public class StepperAdapter extends RecyclerView.Adapter<StepperAdapter.StepViewHolder> {

    private List<Step> steps;
    private Context context;
    private StepperItemListener listener;
    private int completedColor;
    private int defaultTextColor;
    private StepsExecuter stepsExecuter;

    public StepperAdapter(Context context, StepperItemListener listener, List<Step> stepList) {
        this.context = context;
        this.listener = listener;
        this.steps = stepList;

        stepsExecuter = StepsExecuter.getInstance();

        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.disable_color, typedValue, true);
        completedColor = typedValue.data;

        TypedValue typedValue2 = new TypedValue();
        Resources.Theme theme2 = context.getTheme();
        theme2.resolveAttribute(R.attr.title_text_color, typedValue2, true);
        defaultTextColor = typedValue2.data;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stepper_layout, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = steps.get(position);
        stepsExecuter.setCurrentPosition(position);

        holder.stepCounter.setText(String.valueOf(step.getStepIndex()));
        holder.stepTitle.setText(step.getName());

        SoftScriptReader reader = new SoftScriptReader();

        if (step.isDone()) {
            holder.stepTitle.setTextColor(completedColor);
            /*
            Drawable backgroundDrawable = DrawableCompat.wrap(holder.stepCounter.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, completedColor);
             */
            holder.stepCounter.setBackground(context.getResources().getDrawable(R.drawable.step_check_disabled_suface));
        } else {
            holder.stepTitle.setTextColor(defaultTextColor);
            /*
            Drawable backgroundDrawable = DrawableCompat.wrap(holder.stepCounter.getBackground()).mutate();
            DrawableCompat.setTint(backgroundDrawable, context.getResources().getColor(R.color.android_green));
             */
            holder.stepCounter.setBackground(context.getResources().getDrawable(R.drawable.step_check_suface));
        }

        if (step.isExpand()) {
            holder.stepActionsLayout.setVisibility(View.VISIBLE);

            String script = step.getSoftScript();

            if (script != null) {
                reader.setScript(script);
                reader.readScript();
            }

            List<String> views = reader.getViews();
            List<String> values = reader.getValues();

            for (int i = 0; i < views.size(); i++) {
                switch (views.get(i)) {
                    case SoftScriptGenerator.ELEMENT_TIMER:
                        holder.timerView.setVisibility(View.VISIBLE);
                        holder.timerPlayPauseBtn.setVisibility(View.VISIBLE);
                        String timerValue = values.get(i).substring(0, values.get(i).length() - 1);

                        holder.startTimer(timerValue);
                        break;
                    case SoftScriptGenerator.ELEMENT_STOPWATCH:
                        holder.stopwatchView.setVisibility(View.VISIBLE);
                        String stopwatchValue = values.get(i).substring(0, values.get(i).length() - 1);

                        holder.startStopwatch(stopwatchValue);
                        break;
                }
            }
        } else {
            holder.stepActionsLayout.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            listener.onStepClicked(step, holder.getAdapterPosition());

            handleStepChanged(holder.getAdapterPosition());
        });
    }

    public void handleStepChanged(int adapterPosition) {
        if (adapterPosition == 0) {
            boolean isDone = steps.get(adapterPosition).isDone();
            steps.get(adapterPosition).setDone(!isDone);
            notifyItemChanged(0);

            if (adapterPosition < steps.size()) {
                for (int m = adapterPosition + 1; m < steps.size(); m++) {
                    steps.get(m).setDone(false);
                    notifyItemChanged(m);
                }
            }
        } else {
            for (int i = 0; i <= adapterPosition; i++) {
                steps.get(i).setDone(true);
                notifyItemChanged(i);
            }
            if (adapterPosition < steps.size()) {
                for (int p = adapterPosition + 1; p < steps.size(); p++) {
                    steps.get(p).setDone(false);
                    notifyItemChanged(p);
                }
            }
        }
        for (int i = 0; i < steps.size(); i++) {
            steps.get(i).setExpand(i == adapterPosition);
        }
    }

    public Step getStepAt(int position) {
        return steps.get(position);
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    static class StepViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView stepCounter;
        AppCompatTextView stepTitle;
        ConstraintLayout stepLayout;
        ConstraintLayout stepActionsLayout;
        AppCompatTextView timerView;
        AppCompatTextView stopwatchView;
        AppCompatTextView stepStatusView;
        AppCompatImageView timerPlayPauseBtn;
        StepsExecuter executer;

        private static final String MILLIS_FORMAT = "%03d";
        private static final long MILLIS = 10000;
        int secondsLeft = 0;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);

            stepCounter = itemView.findViewById(R.id.step_count);
            stepTitle = itemView.findViewById(R.id.step_header);
            stepLayout = itemView.findViewById(R.id.parent_step);
            stepActionsLayout = itemView.findViewById(R.id.step_actions_layout);

            timerView = stepActionsLayout.findViewById(R.id.timer_tv);
            stopwatchView = stepActionsLayout.findViewById(R.id.stopwatch_tv);
            stepStatusView = stepActionsLayout.findViewById(R.id.step_status_tv);
            timerPlayPauseBtn = stepActionsLayout.findViewById(R.id.action_timer_play_pause);

            executer = StepsExecuter.getInstance();
        }

        public void startTimer(String value) {
            new CountDownTimer(MILLIS, 1) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (Math.round((float) millisUntilFinished / 1000.0f) != secondsLeft) {
                        secondsLeft = Math.round((float) millisUntilFinished / 1000.0f);
                    }
                    long roundMillis = secondsLeft * 1000;
                    if (roundMillis == MILLIS) {
                        timerView.setText(secondsLeft + "." + String.format(MILLIS_FORMAT, 0));
                    } else {
                        timerView.setText(secondsLeft + "." + String.format(MILLIS_FORMAT, millisUntilFinished % 1000));
                    }
                }

                @Override
                public void onFinish() {
                    executer.moveToStep();
                }
            }.start();
        }

        public void startStopwatch(String value) {

        }
    }

    public void moveToStep(int position) {
        steps.get(position).setDone(false);

        for (int i = 0; i < steps.size(); i++) {
            if (i == position) {
                steps.get(i).setExpand(true);
                steps.get(i).setDone(false);
            } else {
                steps.get(i).setExpand(false);
                steps.get(i).setDone(true);
            }
            notifyItemChanged(i);
        }
    }
}
