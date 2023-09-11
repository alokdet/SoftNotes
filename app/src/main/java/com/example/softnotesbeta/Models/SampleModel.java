package com.example.softnotesbeta.Models;

public class SampleModel {
    private int stepCount;
    private String stepTitle;
    private String stepDescription;

    public SampleModel(int stepCount, String stepTitle, String stepDescription) {
        this.stepCount = stepCount;
        this.stepTitle = stepTitle;
        this.stepDescription = stepDescription;
    }

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public String getStepTitle() {
        return stepTitle;
    }

    public void setStepTitle(String stepTitle) {
        this.stepTitle = stepTitle;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }
}
