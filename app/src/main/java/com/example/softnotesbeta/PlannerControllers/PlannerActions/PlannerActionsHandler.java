package com.example.softnotesbeta.PlannerControllers.PlannerActions;


import androidx.constraintlayout.widget.ConstraintLayout;

public class PlannerActionsHandler {

    private int currentLayout;
    private ConstraintLayout layout;
    private AddElementLayout addElementLayout;
    private static PlannerActionsHandler INSTANCE = null;
    private PlannerActionsHandler() {

    }

    public static synchronized PlannerActionsHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlannerActionsHandler();
        }
        return INSTANCE;
    }

    public void initialise(ConstraintLayout layout) {
        this.layout = layout;

        addElementLayout = new AddElementLayout(layout);

        setDefaultLayout();
    }

    private void setDefaultLayout() {
        if (currentLayout != 0) {
            removeCurrentLayout();
        }
        addElementLayout.activate();
    }

    public void setAddElementLayout() {
        removeCurrentLayout();
        addElementLayout.activate();
    }

    public void removeCurrentLayout() {
        switch (currentLayout) {
            case 0:
                addElementLayout.deActivate();
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
