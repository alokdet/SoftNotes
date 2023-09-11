package com.example.softnotesbeta.WorkspaceControllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.R;
import com.example.softnotesbeta.TextTranslator;

public class TranslateController {

    private ConstraintLayout layout;
    private View view;
    private Context context;
    private NoteControllerHandler handler;
    private TextTranslator translator;

    public TranslateController(ConstraintLayout layout) {
        this.layout = layout;
        this.context = layout.getContext();
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.translate_control_layout, layout, false);
        handler = NoteControllerHandler.getInstance();
        translator = new TextTranslator();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentActionSet(1);

        AppCompatImageView actionCloseTranslation = (AppCompatImageView) view.findViewById(R.id.previous_action_btn);
        AppCompatSpinner fromSpinner = (AppCompatSpinner) view.findViewById(R.id.from_language_spinner);
        AppCompatSpinner toSpinner = (AppCompatSpinner) view.findViewById(R.id.to_language_spinner);
        AppCompatImageView actionProceedTranslation = (AppCompatImageView) view.findViewById(R.id.done_translation);

        actionCloseTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCurrentActionSet();
                handler.setRegularController();
                handler.expandLayout();
                handler.setRegularHeader();
            }
        });

        actionProceedTranslation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                translator.translate(handler.getNoteText());
            }
        });

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                translator.setFromLanguage(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter fromAdapter = new ArrayAdapter(context, R.layout.spinner_item_layout, translator.getLanguages());
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                translator.setToLanguage(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter toAdapter = new ArrayAdapter(context, R.layout.spinner_item_layout, translator.getLanguages());
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);

    }

    public void deActivate() {
        layout.removeView(view);
    }
}
