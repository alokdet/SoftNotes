package com.example.softnotesbeta.PlannerControllers.PlannerActions;

import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.R;

public class AddElementLayout {

    private ConstraintLayout layout;
    private View view;
    private PlannerActionsHandler handler;

    public AddElementLayout(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.paint_layout, layout, false);

        handler = PlannerActionsHandler.getInstance();
    }

    private void adjustLayoutSize() {

    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentLayout(1);
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
