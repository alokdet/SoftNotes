package com.example.softnotesbeta;

import android.view.View;

import com.example.softnotesbeta.Entities.Step;

public interface ConfigureStepContentListener {
    void onStepClicked(Step step, View view, int position);
}
