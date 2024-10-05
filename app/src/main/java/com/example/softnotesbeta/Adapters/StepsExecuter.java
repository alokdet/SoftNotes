package com.example.softnotesbeta.Adapters;

import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.WorkspaceControllers.NoteControllerHandler;

public class StepsExecuter {

    private StepperAdapter stepsAdapter;
    private ContentPageAdapter contentAdapter;
    private int currentPosition;
    private Step currentStep;

    private static StepsExecuter INSTANCE = null;
    private StepsExecuter() {}

    public static synchronized StepsExecuter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StepsExecuter();
        }
        return INSTANCE;
    }

    public void init(StepperAdapter stepperAdapter, ContentPageAdapter stepContentPager) {
        this.stepsAdapter = stepperAdapter;
        this.contentAdapter = stepContentPager;
    }

    public void startTask() {
        //stepsAdapter.handleStepChanged(0);
    }

    public void moveToStep() {
        //stepsAdapter.handleStepChanged(currentPosition);
    }

    public void setCurrentPosition(int position) {
        this.currentPosition = position;
        currentStep = stepsAdapter.getStepAt(position);
    }

    public int getCurrentPosition() {
        return this.currentPosition;
    }
}
