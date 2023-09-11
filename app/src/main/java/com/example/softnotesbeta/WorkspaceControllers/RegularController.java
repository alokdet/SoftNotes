package com.example.softnotesbeta.WorkspaceControllers;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
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
import com.example.softnotesbeta.Models.AttachmentsModel;
import com.example.softnotesbeta.Models.SaveNoteModel;
import com.example.softnotesbeta.R;

import java.util.logging.Handler;

public class RegularController implements SaveNoteModel.OnBackPressed, AttachmentsModel.OnImageImported, TextToSpeech.OnInitListener {

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
        AttachmentsModel.getInstance().setListener(this::onImageImported);
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
        AppCompatImageView actionPaginateText = (AppCompatImageView) view.findViewById(R.id.paginate_note);
        AppCompatImageView actionInsertImage = (AppCompatImageView) view.findViewById(R.id.attach_img);
        AppCompatImageView actionLaunchFloatingView = (AppCompatImageView) view.findViewById(R.id.floating_view);
        AppCompatImageView actionStartTTS = (AppCompatImageView) view.findViewById(R.id.action_tts);
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
                handler.removeCurrentActionSet();
                handler.setTranslateController();
                handler.collapseLayout();
                handler.setTranslateHeader();
            }
        });

        actionPaginateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.page();
            }
        });

        actionInsertImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmentsModel.getInstance().actionTriggered(1);
            }
        });

        actionLaunchFloatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AttachmentsModel.getInstance().launchFloatingView();
            }
        });

        actionStartTTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(handler.getNoteText(), TextToSpeech.QUEUE_FLUSH, null, "doesn't matter yet");
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

        if (noteText1.length() > 150) {
            preview = new Preview(id, handler.getTitle(), noteText1.substring(0, 150), handler.getDate());
        } else {
            preview = new Preview(id, handler.getTitle(), noteText1, handler.getDate());
        }

        previewDao.insertNoteToDatabase(preview);
        SaveNoteModel.getInstance().createSaveUpdate();
    }

    private void updateNote(long id) {
        String noteText1 = handler.getNoteText();
        Preview preview = previewDao.getPreview(id);
        Note note = noteDao.getNote(preview.getNoteId());

        if (noteText1.length() > 150) {
            preview.setTitle(handler.getTitle());
            preview.setPreview(noteText1.substring(0, 150));
            preview.setDate(handler.getDate());
        } else {
            preview.setTitle(handler.getTitle());
            preview.setPreview(noteText1);
            preview.setDate(handler.getDate());
        }

        note.setTitle(handler.getTitle());
        note.setText(noteText1);
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
        layout.setVisibility(View.VISIBLE);
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
    public void onImageImported(Bitmap bitmap) {
        ImageSpan imageSpan = new ImageSpan(bitmap);
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
        stringBuilder.append(handler.getNoteText());
        String imgId = "[img=1]";
        int selectionStart = handler.getSelectionStart();
        stringBuilder.replace(handler.getSelectionStart(), handler.getSelectionEnd(), imgId);
        stringBuilder.setSpan(imageSpan, selectionStart, selectionStart + imgId.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        handler.setNoteText(stringBuilder);
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
