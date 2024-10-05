package com.example.softnotesbeta.Models;

import java.util.List;

public class Step {
    private int stepIndex;
    private String name;
    private String content;
    private String softScript;
    private String contentSoftScript;
    private boolean done;

    private boolean expand;

    public Step() {

    }

    public Step(String name) {
        this.name = name;
    }

    public Step(int index, String name) {
        this.stepIndex = index;
        this.name = name;
        this.done = false;
    }

    public Step(int index, String name, String content) {
        this.stepIndex = index;
        this.name = name;
        this.content = content;
        this.done = false;
    }

    public String getContentSoftScript() {
        return contentSoftScript;
    }

    public void setContentSoftScript(String contentSoftScript) {
        this.contentSoftScript = contentSoftScript;
    }

    public int getStepIndex() {
        return stepIndex;
    }

    public boolean isExpand() {
        return expand;
    }

    public String getSoftScript() {
        return softScript;
    }

    public void setSoftScript(String script) {
        this.softScript = script;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
