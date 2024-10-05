package com.example.softnotesbeta.WorkspaceControllers;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.transition.Slide;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.MainActivity2;
import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.Models.SaveNoteModel;
import com.example.softnotesbeta.Models.TranslateCallbacks;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.TextScannerActivity;

import java.util.List;

public class RegularController implements SaveNoteModel.OnBackPressed, TextToSpeech.OnInitListener {

    private ConstraintLayout layout;
    private View view;
    private NoteControllerHandler handler;
    private NotesDatabase database;
    private NoteDao noteDao;
    private PreviewDao previewDao;
    private TextToSpeech textToSpeech;

    public RegularController(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.regular_actions, layout, false);

        handler = NoteControllerHandler.getInstance();
        SaveNoteModel.getInstance().setBackListener(this::onBackPressed);
        database = NotesDatabase.getInstance(layout.getContext());
        noteDao = database.noteDao();
        previewDao = database.previewDao();

        textToSpeech = new TextToSpeech(layout.getContext(), this::onInit);
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentActionSet(0);

        AppCompatImageView actionTextAlign = (AppCompatImageView) view.findViewById(R.id.alignment);
        AppCompatImageView actionTranslateText = (AppCompatImageView) view.findViewById(R.id.translate);
        AppCompatImageView actionExtractText = (AppCompatImageView) view.findViewById(R.id.extract_text);
        AppCompatImageView actionTTS = (AppCompatImageView) view.findViewById(R.id.action_tts);
        AppCompatImageView actionSaveNote = (AppCompatImageView) view.findViewById(R.id.save_note);

        actionTextAlign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (handler.getAlignment()) {
                    case View.TEXT_ALIGNMENT_TEXT_START:
                        handler.setAlignment(View.TEXT_ALIGNMENT_CENTER);
                        break;
                    case View.TEXT_ALIGNMENT_CENTER:
                        handler.setAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                        break;
                    case View.TEXT_ALIGNMENT_TEXT_END:
                        handler.setAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        break;
                }
            }
        });

        actionTranslateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                handler.removeCurrentActionSet();
                handler.removeCurrentHeader();
                handler.setTranslateController();
                handler.collapseLayout();
                handler.setTranslateHeader();

                 */
                TranslateCallbacks.getInstance().expand();
               // actionTranslateText.setBackground(layout.getContext().getResources().getDrawable(R.drawable.note_preview_background));
               // actionTranslateText.setPadding(5, 5, 5, 5);
                handler.initTranslatePager();

            }
        });

        actionExtractText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handler.page();
                Intent intent = new Intent(layout.getContext(), TextScannerActivity.class);
                layout.getContext().startActivity(intent);
            }
        });

        actionTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.startTTS();
            }
        });

        actionSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveNote();

                if (previewDao.isNoteExist(SaveNoteModel.getInstance().getId()) == 0) {
                    saveNote();
                } else {
                    updateNote(SaveNoteModel.getInstance().getId());
                }

            }
        });
    }

    private void saveNote() {
        long id = noteDao.insertNoteToDatabase(handler.getNoteModel());

        String noteText1 = handler.getNoteText();
        Preview preview;

        switch (handler.getNoteType()) {
            case "text":
                if (noteText1.length() > 150) {
                    preview = new Preview(id, handler.getTitle(), noteText1.substring(0, 150), handler.getDate(), handler.getNoteType());
                } else {
                    preview = new Preview(id, handler.getTitle(), noteText1, handler.getDate(), handler.getNoteType());
                }
                previewDao.insertNoteToDatabase(preview);
                break;
            case "list":
                preview = new Preview(id, handler.getTitle(), makeListPreview(handler.getList()).toString(), handler.getDate(), handler.getNoteType());
                previewDao.insertNoteToDatabase(preview);
                break;
            case "image":
                preview = new Preview(id, handler.getTitle(), "", handler.getDate(), handler.getNoteType());
                previewDao.insertNoteToDatabase(preview);
                break;
        }
        SaveNoteModel.getInstance().createSaveUpdate();
    }

    private CharSequence makeListPreview(List<ListItem> list) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int index = 0; index < list.size();  index++) {
            String text = list.get(index).getText();
            String line = text + (index < list.size() - 1 ? "\n" : "");

            Spannable spannable = new SpannableString(line);
            spannable.setSpan(new BulletSpan(15, Color.GRAY), 0, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(spannable);
        }
        return spannableStringBuilder;
    }

    private void updateNote(long id) {
        String noteText1 = handler.getNoteText();
        Preview preview = previewDao.getPreview(id);
        Note note = noteDao.getNote(preview.getNoteId());

        switch (handler.getNoteType()) {
            case "text":
                if (noteText1.length() > 150) {
                    preview.setTitle(handler.getTitle());
                    preview.setPreview(noteText1.substring(0, 150));
                    preview.setDate(handler.getDate());
                } else {
                    preview.setTitle(handler.getTitle());
                    preview.setPreview(noteText1);
                    preview.setDate(handler.getDate());
                }
                note.setText(noteText1);
                break;
            case "list":
                preview.setTitle(handler.getTitle());
                preview.setDate(handler.getDate());
                note.setList(handler.getList());
                break;
            case "image":
                preview.setTitle(handler.getTitle());
                preview.setDate(handler.getDate());
                note.setText(noteText1);
                break;
        }

        note.setTitle(handler.getTitle());
        note.setDate(handler.getDate());

        noteDao.updateNote(note);
        previewDao.updatePreview(preview);
        SaveNoteModel.getInstance().createSaveUpdate();
    }

    public void reveal() {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(layout);

        TransitionManager.beginDelayedTransition((ViewGroup) layout.getParent(), transition);
        layout.setVisibility(View.GONE);
    }

    public void hide() {
        Transition transition = new Slide(Gravity.BOTTOM);
        transition.setDuration(500);
        transition.addTarget(layout);

        TransitionManager.beginDelayedTransition((ViewGroup) layout.getParent(), transition);
        layout.setVisibility(View.GONE);
    }

    public void deActivate() {
        layout.removeView(view);
    }

    @Override
    public void onBackPressed(String mode, long id) {
       // saveNote();

        if (previewDao.isNoteExist(id) == 0) {
            saveNote();
        } else {
            updateNote(id);
        }

    }

    @Override
    public void onInit(int status) {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {

            }

            @Override
            public void onError(String utteranceId) {

            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                Spannable highlightedText = new SpannableString(handler.getNoteText());
                highlightedText.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                handler.setNoteText(highlightedText);
            }
        });
    }
}
