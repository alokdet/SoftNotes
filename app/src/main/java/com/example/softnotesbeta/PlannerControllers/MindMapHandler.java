package com.example.softnotesbeta.PlannerControllers;

import android.graphics.Bitmap;

import androidx.constraintlayout.widget.ConstraintLayout;

public class MindMapHandler {

    private int currentLayout;
    private ConstraintLayout layout;
    private RegularLayout regularLayout;
    private PaintLayout paintLayout;

    private static MindMapHandler INSTANCE = null;
    private MindMapHandler() {

    }

    public static synchronized MindMapHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MindMapHandler();
        }
        return INSTANCE;
    }

    public void initialise(ConstraintLayout layout) {
        this.layout = layout;

        regularLayout = new RegularLayout(layout);
        paintLayout = new PaintLayout(layout);

        setDefaultLayout();
    }

    private void setDefaultLayout() {
        if (currentLayout != 0) {
            removeCurrentLayout();
        }
        regularLayout.activate();
    }

    public void setRegularLayout() {
        removeCurrentLayout();
        regularLayout.activate();
    }

    public void setPaintLayout() {
        removeCurrentLayout();
        paintLayout.activate();
    }

    public void addPaintElement(Bitmap bitmap) {
        regularLayout.addElement(bitmap);
    }

    public void removeCurrentLayout() {
        switch (currentLayout) {
            case 0:
                regularLayout.deActivate();
                break;
            case 1:
                paintLayout.deActivate();
                break;
        }
    }

    public int getCurrentActionSet() {
        return currentLayout;
    }

    public void setCurrentLayout(int currentLayout) {
        this.currentLayout = currentLayout;
    }
}
