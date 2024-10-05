package com.example.softnotesbeta.Application.SoftScript;

import java.util.ArrayList;
import java.util.List;

public class SoftScriptReader {

    private String script;
    private int rowIndex = -1;
    private List<String> nodes;
    private List<String> values;
    private List<String> rows;

    public SoftScriptReader() {
        rows = new ArrayList<>();
        nodes = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void readScript() {
        StringBuilder row = new StringBuilder();
        StringBuilder node = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < script.length(); i++) {
            char character = script.charAt(i);

            switch (character) {
                case '[':
                    rowIndex++;
                    break;
                case '=':
                    nodes.add(node.toString());
                    node.delete(0, node.length());
                    break;
                case ']':
                    rows.add(row.toString());
                    row.delete(0, row.length());
                    values.add(value.toString());
                    value.delete(0, value.length());
                    break;
                default:
                    if (row.toString().contains("=")) {
                        value.append(character);
                    } else {
                        node.append(character);
                    }
            }
            row.append(character);
        }
    }

    public List<String> getRows() {
        return rows;
    }

    public List<String> getNodes() {
        return this.nodes;
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
