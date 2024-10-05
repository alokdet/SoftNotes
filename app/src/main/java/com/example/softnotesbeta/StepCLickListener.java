package com.example.softnotesbeta;

import android.view.View;

import com.example.softnotesbeta.Models.Step;

public interface StepCLickListener {
    void onStepClicked(Step step, View view, int position);
}
