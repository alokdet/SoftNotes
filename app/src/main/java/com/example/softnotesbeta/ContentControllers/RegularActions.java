package com.example.softnotesbeta.ContentControllers;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.ContentControllers.ContentModels.ActionsToActivity;
import com.example.softnotesbeta.R;

public class RegularActions {

    private ConstraintLayout layout;
    private View view;

    private ContentControllerHandler handler;

    public RegularActions(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.regular_content_actions, layout, false);

        handler = ContentControllerHandler.getInstance();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentActionSet(0);

        AppCompatImageView actionTimer = (AppCompatImageView) view.findViewById(R.id.action_conig_timer);
        AppCompatImageView actionStopwatch = (AppCompatImageView) view.findViewById(R.id.action_conig_stopwatch);
        AppCompatImageView actionChart = (AppCompatImageView) view.findViewById(R.id.action_conig_graph);
        AppCompatImageView actionInput = (AppCompatImageView) view.findViewById(R.id.action_conig_input);
        AppCompatImageView actionImage = (AppCompatImageView) view.findViewById(R.id.action_conig_image);
        AppCompatImageView actionVideo = (AppCompatImageView) view.findViewById(R.id.action_insert_clip);
        AppCompatImageView actionAudio = (AppCompatImageView) view.findViewById(R.id.action_insert_audio);
        AppCompatImageView actionDocument = (AppCompatImageView) view.findViewById(R.id.action_insert_document);
        AppCompatImageView actionNote = (AppCompatImageView) view.findViewById(R.id.action_insert_note);

        actionTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handler.setTimeController(0);
                handler.setTimer("30");
            }
        });

        actionStopwatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handler.setTimeController(1);
                handler.setStopwatch("30");
            }
        });

        actionChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        actionInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        actionImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionsToActivity.getInstance().takeImage();
            }
        });

        actionVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionsToActivity.getInstance().takeVideo();
            }
        });

        actionAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionsToActivity.getInstance().takeAudio();
            }
        });

        actionDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionsToActivity.getInstance().takeDocument();
            }
        });

        actionNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
