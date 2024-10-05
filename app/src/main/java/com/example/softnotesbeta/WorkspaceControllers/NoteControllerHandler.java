package com.example.softnotesbeta.WorkspaceControllers;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.Workspace;

import java.util.List;

public class NoteControllerHandler {

    private ConstraintLayout layout1;
    private ConstraintLayout layout2;
    private ConstraintLayout layout3;
    private String noteType;
    private int currentActionSet;
    private int currentHeader;
    private RegularController regularActions;
    private ImageActions imageActions;
    private WriteController writeController;
    private ImageController imageController;
    private ListController listController;
    private RegularHeader regularHeader;

    private static NoteControllerHandler INSTANCE = null;
    private NoteControllerHandler() {}

    public static synchronized NoteControllerHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new NoteControllerHandler();
        }
        return INSTANCE;
    }

    public void initialise(ConstraintLayout layout1, ConstraintLayout layout2, ConstraintLayout layout3, String noteType, Workspace activity) {
        this.layout1 = layout1;
        this.layout2 = layout2;
        this.layout3 = layout3;
        this.noteType = noteType;

        regularActions = new RegularController(layout1);
        imageActions = new ImageActions(layout1);

        regularHeader = new RegularHeader(layout2);

        writeController = new WriteController(layout3, activity);
        imageController = new ImageController(layout3);
        listController = new ListController(layout3);

        setDefaultActionSet();
        setDefaultHeader();
        setDefaultNoteLayout();
    }

    private void setDefaultNoteLayout() {
        switch (noteType) {
            case "text":
                writeController.activate();
                break;
            case "image":
                imageController.activate();
                break;
            case "list":
                listController.activate();
                break;
        }
    }

    private void setDefaultActionSet() {
        switch (noteType) {
            case "text":
                if (currentActionSet != 0) {
                    removeCurrentActionSet();
                }
                regularActions.activate();
                break;
            case "image":
                imageActions.activate();
                break;
            case "list":
                listController.activate();
                break;
        }
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

    public void setRegularHeader() {
        removeCurrentHeader();
        regularHeader.activate();
    }

    public void setTranslatedText(String text) {
        writeController.setTranslatedText(text);
    }

    public Note getNoteModel() {
        Note note;
        switch (noteType) {
            case "text":
                note = new Note(regularHeader.getNoteTitle(), writeController.getNote(), writeController.getDate(), noteType);
                return note;
            case "list":
                note = new Note(regularHeader.getNoteTitle(), "", listController.getDate(), noteType);
                note.setList(listController.getItemsList());
                return note;
            case "image":
                note = new Note(regularHeader.getNoteTitle(), imageController.getGeneratedSoftScript(), imageController.getDate(), noteType);
                return note;
        }
        return null;
    }

    public List<ListItem> getList() {
        return listController.getItemsList();
    }

    public void handleImageNoteInsertion(String path) {
        imageController.handleImageInsertion(path);
    }

    public void sss() {
        //regularHeader.setNoteTitle(imageController.getSoftScript());
    }

    public void setNote(Note note) {
        switch (noteType) {
            case "text":
                regularHeader.setNoteTitle(note.getTitle());
                writeController.setNote(note.getText());
                writeController.setDate(note.getDate());
                break;
            case "list":
                regularHeader.setNoteTitle(note.getTitle());
                listController.setItemsList(note.getList());
                listController.displayList();
                listController.setDate(note.getDate());
                break;
            case "image":
                regularHeader.setNoteTitle(note.getTitle());
                imageController.setSoftScript(note.getText());
                imageController.displayImages();
                imageController.setDate(note.getDate());
                break;
        }
    }

    public String getNoteType() {
        return this.noteType;
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
        writeController.changeMode(1);
    }

    public void initTranslatePager() {
        writeController.changeMode(2);
    }

    public void normalTextMode() {
        writeController.changeMode(0);
    }

    public void startTTS() {
        writeController.startTTS();
    }

    public void stopTTS() {

    }

    public void startHeader() {
        writeController.setHeader(true);
    }

    public void stopHeader() {
        writeController.setHeader(false);
    }

    public boolean getHeaderState() {
        return writeController.getHeader();
    }

    public void setNoteDetails(String details) {
        regularHeader.setNoteTextDetails(details);
    }

    public String getNoteText() {
        switch (noteType) {
            case "text":
                return writeController.getNote();
            case "list":
                return listController.getItemsList().toString();
            case "image":
                return imageController.getGeneratedSoftScript();
        }
        return null;
    }

    public void setNoteType(String noteType) {
        this.noteType = noteType;
    }

    public void setNoteText(String text) {
        switch (noteType) {
            case "text":
                writeController.setNote(text);
                break;
            case "image":
                imageController.displayImages();
                break;
        }
    }

    public void setNoteText(SpannableStringBuilder text) {
        writeController.setNote(text);
    }

    public void setNoteText(Spannable text) {
        writeController.setNote(text);
    }

    public String getDate() {
        switch (noteType) {
            case "text":
                return writeController.getDate();
            case "list":
                return listController.getDate();
            case "image":
                return imageController.getDate();
        }
        return null;
    }

    public void setDate(String date) {
        switch (noteType) {
            case "text":
                writeController.setDate(date);
                break;
            case "list":
                listController.setDate(date);
                break;
            case "image":
                imageController.setDate(date);
                break;
        }
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
        }
    }

    public void removeCurrentHeader() {
        switch (currentHeader) {
            case 1:
                regularHeader.deActivate();
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
