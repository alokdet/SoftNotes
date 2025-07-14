package com.example.softnotesbeta.Models;

import android.text.SpannableStringBuilder;

public class ListItem {

    private SpannableStringBuilder text;
    private String stringText;
    private boolean isChecked;

    public ListItem() {
        text = new SpannableStringBuilder();
    }

    public ListItem(String text) {
        this.text = new SpannableStringBuilder(text);
        this.stringText = text;
    }
    public String getText() {
        return text.toString();
    }

    public void setText(String text) {
        this.text.append(text);
    }

    public String getStringText() {
        return stringText;
    }

    public void setStringText(String stringText) {
        this.stringText = stringText;
    }

    public void setSpannedText(SpannableStringBuilder stringBuilder) {
        this.text = stringBuilder;
    }

    public SpannableStringBuilder getSpannedText() {
        return this.text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
