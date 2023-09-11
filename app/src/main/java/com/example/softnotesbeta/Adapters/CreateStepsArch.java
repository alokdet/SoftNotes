package com.example.softnotesbeta.Adapters;

import android.widget.LinearLayout;

import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.Views.StepView;
import com.example.softnotesbeta.WorkspaceControllers.NoteControllerHandler;

import java.util.ArrayList;
import java.util.List;

public class CreateStepsArch {

    private LinearLayout layout;
    private List<Step> stepList = new ArrayList<>();
    private int count = 0;
    private int currentFocus;
    private int contentPosition;

    private static CreateStepsArch INSTANCE = null;
    private CreateStepsArch() {}

    public static synchronized CreateStepsArch getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CreateStepsArch();
        }
        return INSTANCE;
    }

    public void initialize(LinearLayout layout) {
        this.layout = layout;

        displayEssentials();
    }

    private void displayEssentials() {
        addStep();
    }

    public void addStepData(String name) {
        stepList.get(currentFocus).setName(name);
    }

    public void addScriptData(String script) {
        stepList.get(currentFocus).setSoftScript(script);
    }

    public void addStep() {
        Step step = new Step();
        stepList.add(step);
        step.setStepIndex(stepList.size());

        StepView view = new StepView(layout);
        view.activate();
    }

    public void setContentText(String text) {
        stepList.get(contentPosition).setContent(text);
    }

    public List<Step> getStepList() {
        return this.stepList;
    }

    public int getCount() {
        return this.count;
    }

    public int getCurrentFocus() {
        return currentFocus;
    }

    public void setCurrentFocus(int index) {
        this.currentFocus = index;
    }

    public int getContentPosition() {
        return contentPosition;
    }

    public void setContentPosition(int contentPosition) {
        this.contentPosition = contentPosition;
    }
}
