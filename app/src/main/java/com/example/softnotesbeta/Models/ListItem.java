package com.example.softnotesbeta.Models;

public class ListItem {

    private String text;
    private boolean isChecked;

    public ListItem() {

    }

    public ListItem(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
