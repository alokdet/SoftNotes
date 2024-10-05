package com.example.softnotesbeta.ContentControllers;

import com.example.softnotesbeta.Application.SoftScript.SoftScriptGenerator;

public class ContentControllerHandler {
    private SoftScriptGenerator scriptGenerator;
    private SoftScriptGenerator contentScriptGenerator;

    private static ContentControllerHandler INSTANCE = null;
    private ContentControllerHandler() {}

    public static synchronized ContentControllerHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentControllerHandler();
        }
        return INSTANCE;
    }

    public void initialise() {

        scriptGenerator = new SoftScriptGenerator();
        contentScriptGenerator = new SoftScriptGenerator();
    }

    public void setTimer(String value) {
       // scriptGenerator.addElement(SoftScriptGenerator.TIMER, value);
    }

    public void setStopwatch(String value) {
        //scriptGenerator.addElement(SoftScriptGenerator.STOPWATCH, value);
    }

    public void setImage(String uri) {
       // contentScriptGenerator.addElement(SoftScriptGenerator.IMAGE, uri);
    }

    public void addContent() {
       // scriptGenerator.saveScript();
       // contentScriptGenerator.saveContentScript();
    }
}
