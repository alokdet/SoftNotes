package com.example.softnotesbeta.ContentControllers;

import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.R;

public class ChartActions {

    private ConstraintLayout layout;
    private View view;

    private ContentControllerHandler handler;

    public ChartActions(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.regular_actions, layout, false);

        handler = ContentControllerHandler.getInstance();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentActionSet(3);
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
