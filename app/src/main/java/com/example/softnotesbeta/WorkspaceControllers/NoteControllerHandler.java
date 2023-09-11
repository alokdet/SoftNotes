package com.example.softnotesbeta.WorkspaceControllers;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.Entities.Note;

public class NoteControllerHandler {

    private ConstraintLayout layout1;
    private ConstraintLayout layout2;
    private ConstraintLayout layout3;
    private int currentActionSet;
    private int currentHeader;
    private RegularController regularActions;
    private TranslateController translateActions;
    private WriteController writeController;
    private RegularHeader regularHeader;
    private TranslateHeader translateHeader;

    private static NoteControllerHandler INSTANCE = null;
    private NoteControllerHandler() {}

    public static synchronized NoteControllerHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoteControllerHandler();
        }
        return INSTANCE;
    }

    public void initialise(ConstraintLayout layout1, ConstraintLayout layout2, ConstraintLayout layout3) {
        this.layout1 = layout1;
        this.layout2 = layout2;
        this.layout3 = layout3;

        regularActions = new RegularController(layout1);
        translateActions = new TranslateController(layout1);

        regularHeader = new RegularHeader(layout2);
        translateHeader = new TranslateHeader(layout2);

        writeController = new WriteController(layout3);

        setDefaultActionSet();
        setDefaultHeader();
        setDefaultNoteLayout();
    }

    private void setDefaultNoteLayout() {
        writeController.activate();
    }

    private void setDefaultActionSet() {
        if (currentActionSet != 0) {
            removeCurrentActionSet();
        }
        regularActions.activate();
    }

    private void setDefaultHeader() {
        if (currentHeader != 0) {
            removeCurrentHeader();
        }
        regularHeader.activate();
    }

    public void setRegularController() {
        regularActions.activate();
    }

    public void setTranslateController() {
        translateActions.activate();
    }

    public void setRegularHeader() {
        removeCurrentHeader();
        regularHeader.activate();
    }
    public void setTranslateHeader() {
        removeCurrentHeader();
        translateHeader.activate();
    }

    public void setTranslatedText(String text) {
        translateHeader.setTranslatedText(text);
    }

    public Note getNoteModel() {
        Note note = new Note(regularHeader.getNoteTitle(), writeController.getNote(), writeController.getDate());
        return note;
    }

    public void setNote(Note note) {
        regularHeader.setNoteTitle(note.getTitle());
        writeController.setNote(note.getText());
        writeController.setDate(note.getDate());
    }

    public void setAlignment(int alignment) {
        writeController.setAlignment(alignment);
    }

    public int getSelectionStart() {
        return writeController.getSelectionStart();
    }

    public int getSelectionEnd() {
        return writeController.getSelectionEnd();
    }

    public int getAlignment() {
        return writeController.getAlignment();
    }

    public void page() {
        writeController.changeMode();
    }

    public void setNoteDetails(String details) {
        regularHeader.setNoteTextDetails(details);
    }

    public String getNoteText() {
        return writeController.getNote();
    }

    public void setNoteText(String text) {
        writeController.setNote(text);
    }

    public void setNoteText(SpannableStringBuilder text) {
        writeController.setNote(text);
    }

    public void setNoteText(Spannable text) {
        writeController.setNote(text);
    }

    public String getDate() {
        return writeController.getDate();
    }

    public void setDate(String date) {
        writeController.setDate(date);
    }

    public String getTitle() {
        return regularHeader.getNoteTitle();
    }

    public void revealActions() {
        regularActions.reveal();
    }

    public void hideActions() {
        regularActions.hide();
    }

    public void collapseLayout() {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout3.getLayoutParams();
        int currentMarginTop = params.topMargin;

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                params.topMargin = (int) (3 * currentMarginTop * interpolatedTime);
                layout3.setLayoutParams(params);
            }
        };
        animation.setDuration(500);
        layout3.startAnimation(animation);
    }

    public void expandLayout() {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout3.getLayoutParams();
        int currentMarginTop = params.topMargin;

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                params.topMargin = (int) ((currentMarginTop / 3) * interpolatedTime);
                layout3.setLayoutParams(params);
            }
        };
        animation.setDuration(500);
        layout3.startAnimation(animation);
    }

    public void removeCurrentActionSet() {
        switch (currentActionSet) {
            case 0:
                regularActions.deActivate();
                break;
            case 1:
                translateActions.deActivate();
                break;
        }
    }

    public void removeCurrentHeader() {
        switch (currentHeader) {
            case 1:
                regularHeader.deActivate();
                break;
            case 2:
                translateHeader.deActivate();
                break;
        }
    }

    public int getCurrentActionSet() {
        return currentActionSet;
    }

    public void setCurrentActionSet(int currentActionSet) {
        this.currentActionSet = currentActionSet;
    }

    public int getCurrentHeader() {
        return currentHeader;
    }

    public void setCurrentHeader(int currentHeader) {
        this.currentHeader = currentHeader;
    }
}
