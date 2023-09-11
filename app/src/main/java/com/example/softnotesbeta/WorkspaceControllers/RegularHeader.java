package com.example.softnotesbeta.WorkspaceControllers;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.R;

public class RegularHeader {

    private ConstraintLayout layout;
    private View view;
    private NoteControllerHandler handler;
    private String noteTitle;
    private String noteTextDetails;
    private AppCompatEditText inputTitle;
    private AppCompatTextView displayTextDetails;

    public RegularHeader(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.regular_header, layout, false);
        inputTitle = (AppCompatEditText) view.findViewById(R.id.input_title);
        displayTextDetails = (AppCompatTextView) view.findViewById(R.id.text_details);
        handler = NoteControllerHandler.getInstance();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentHeader(0);
    }

    public String getNoteTitle() {
        noteTitle = inputTitle.getText().toString();
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
        inputTitle.setText(noteTitle);
    }

    public String getNoteTextDetails() {
        return noteTextDetails;
    }

    public void setNoteTextDetails(String noteTextDetails) {
        this.noteTextDetails = noteTextDetails;
        displayTextDetails.setText(noteTextDetails);
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
