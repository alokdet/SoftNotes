package com.example.softnotesbeta.Application.SoftScript;

public class SoftScriptGenerator {
    public static final String ELEMENT_IMAGE = "img";
    public static final String ELEMENT_VIDEO = "vid";
    public static final String ELEMENT_AUDIO = "aud";
    public static final String ELEMENT_TEXT = "txt";
    public static final String ELEMENT_DOCUMENT = "doc";
    private String script = "";

    public SoftScriptGenerator() {

    }

    public void addRow(String node, String value) {
        script += "[" + node + "=" + value + "]";
    }

    public String getScript() {
        return script;
    }
}
