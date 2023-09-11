package com.example.softnotesbeta.Application.SoftScript;

import com.example.softnotesbeta.Adapters.CreateStepsArch;

public class SoftScriptGenerator {

    public static final int TIMER = 0;
    public static final int STOPWATCH = 1;

    public static final String ELEMENT_TIMER = "timer";
    public static final String ELEMENT_STOPWATCH = "stopwatch";

    public static final String CMD_ELEMENT = "ELEMENT:";
    public static final String CMD_VALUE = "VALUE=";

    public static final String TOOL_CHAIN = "-";
    public static final String TOOL_END_LINE = " x";
    private String script = "";

    public SoftScriptGenerator() {

    }

    public void addElement(int element, String value) {
        switch (element) {
            case TIMER:
                script += CMD_ELEMENT + ELEMENT_TIMER + TOOL_CHAIN + CMD_VALUE + value + TOOL_END_LINE;
                break;
            case STOPWATCH:
                script += CMD_ELEMENT + ELEMENT_STOPWATCH + TOOL_CHAIN + CMD_VALUE + value + TOOL_END_LINE;
                break;
        }
    }

    public void saveScript() {
        CreateStepsArch.getInstance().addScriptData(getScript());
    }

    public String getScript() {
        return script;
    }
}
