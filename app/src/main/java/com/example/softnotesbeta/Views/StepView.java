package com.example.softnotesbeta.Views;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.softnotesbeta.Adapters.CreateStepsArch;
import com.example.softnotesbeta.Models.DefineStepModel;
import com.example.softnotesbeta.R;

public class StepView {
    private View view;
    private LinearLayout layout;
    private CreateStepsArch handler;
    private int index;

    public StepView(LinearLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.create_step_item_layout, layout, false);

        handler = CreateStepsArch.getInstance();
    }

    public void activate() {
        layout.addView(view);
        index = layout.indexOfChild(view);
        view.setTag(index);

        AppCompatEditText stepInput = (AppCompatEditText) view.findViewById(R.id.step_input);
        AppCompatImageView actionDefineStep = (AppCompatImageView) view.findViewById(R.id.define_step);

        stepInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    handler.addStep();
                    handler.addStepData(stepInput.getText().toString());
                }
                return true;
            }
        });

        stepInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    handler.setCurrentFocus(index);
                } else {
                    handler.addStepData(stepInput.getText().toString());
                }
            }
        });

       actionDefineStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefineStepModel.getInstance().setStepContent(index);
                handler.setContentPosition(index);
            }
        });
    }
}
