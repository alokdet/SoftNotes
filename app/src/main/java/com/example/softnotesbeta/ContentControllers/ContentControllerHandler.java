package com.example.softnotesbeta.ContentControllers;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.Application.SoftScript.SoftScriptGenerator;

public class ContentControllerHandler {

    private ConstraintLayout layout;
    private int currentActionSet;
    private RegularActions regularActions;
    private TimeActions timeActions;
    private InputActions inputActions;
    private ChartActions chartActions;

    private SoftScriptGenerator scriptGenerator;

    private static ContentControllerHandler INSTANCE = null;
    private ContentControllerHandler() {}

    public static synchronized ContentControllerHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentControllerHandler();
        }
        return INSTANCE;
    }

    public void initialise(ConstraintLayout layout1) {
        this.layout = layout1;

        regularActions = new RegularActions(layout);
        timeActions = new TimeActions(layout);
        inputActions = new InputActions(layout);
        chartActions = new ChartActions(layout);

        scriptGenerator = new SoftScriptGenerator();

        setDefaultActionSet();
    }

    private void setDefaultActionSet() {
        if (currentActionSet != 0) {
            removeCurrentActionSet();
        }
        setRegularController();
    }

    public void setRegularController() {
        removeCurrentActionSet();
        regularActions.activate();
    }

    public void setTimeController(int action) {
        removeCurrentActionSet();
        timeActions.activate();
    }

    public void setInputController() {
        removeCurrentActionSet();
        inputActions.activate();
    }

    public void setChartController() {
        removeCurrentActionSet();
        chartActions.activate();
    }

    public int getCurrentActionSet() {
        return currentActionSet;
    }

    public void setTimer(String value) {
        scriptGenerator.addElement(SoftScriptGenerator.TIMER, value);
    }

    public void setStopwatch(String value) {
        scriptGenerator.addElement(SoftScriptGenerator.STOPWATCH, value);
    }

    public void saveScript() {
        scriptGenerator.saveScript();
    }

    public void setCurrentActionSet(int currentActionSet) {
        this.currentActionSet = currentActionSet;
    }

    public void removeCurrentActionSet() {
        switch (currentActionSet) {
            case 0:
                regularActions.deActivate();
                break;
            case 1:
                timeActions.deActivate();
                break;
            case 2:
                inputActions.deActivate();
                break;
            case 3:
                chartActions.deActivate();
                break;
        }
    }
}
