package com.example.softnotesbeta.Application.SoftScript;

import java.util.ArrayList;
import java.util.List;

public class SoftScriptReader {

    private String script;
    private List<String> views;
    private List<String> values;
    private int insertMode = 0;

    public SoftScriptReader() {
        views = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void readScript() {
        StringBuilder sequence = new StringBuilder();

        for (int i = 0; i < script.length(); i++) {
            char character = script.charAt(i);

            sequence.append(character);

            switch (character) {
                case ':':
                case 'x':
                    insertMode = 0;
                    sequence.delete(0, sequence.length());
                    break;
                case '=':
                    insertMode = 1;
                    sequence.delete(0, sequence.length());
                    break;
                case '-':
                    insertMode = 0;
                    views.add(sequence.toString().substring(0, sequence.length() - 1));
                    sequence.delete(0, sequence.length());
                    break;
                case ' ':
                    if (sequence.charAt(0) == 'x') {
                        insertMode = 0;
                        sequence.delete(0, sequence.length());
                    } else {
                        insertMode = 1;
                        values.add(sequence.toString());
                        sequence.delete(0, sequence.length());
                    }
                    break;
            }
        }
    }

    public List<String> getViews() {
        return this.views;
    }

    public List<String> getValues() {
        return this.values;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }
}
