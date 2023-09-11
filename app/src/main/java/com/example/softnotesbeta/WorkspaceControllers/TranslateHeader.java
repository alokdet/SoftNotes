package com.example.softnotesbeta.WorkspaceControllers;

import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.R;

public class TranslateHeader {

    private ConstraintLayout layout;
    private View view;
    private NoteControllerHandler handler;
    private String translatedText;
    private AppCompatTextView translatedTextView;

    public TranslateHeader(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.translate_header, layout, false);
        handler = NoteControllerHandler.getInstance();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentHeader(1);

        translatedTextView = (AppCompatTextView) view.findViewById(R.id.translated_text);
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
        translatedTextView.setText(translatedText);
        translatedTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
