package com.example.softnotesbeta.PlannerControllers;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.softnotesbeta.Views.DrawBoard;
import com.example.softnotesbeta.R;

public class PaintLayout {

    private ConstraintLayout layout;
    private View view;
    private MindMapHandler handler;

    public PaintLayout(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.paint_layout, layout, false);

        handler = MindMapHandler.getInstance();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentLayout(1);

        DrawBoard drawBoard = (DrawBoard) view.findViewById(R.id.draw_layout);
        AppCompatImageView actionEraseAll = (AppCompatImageView) view.findViewById(R.id.erase_all);
        AppCompatImageView actionUndo = (AppCompatImageView) view.findViewById(R.id.undo);
        AppCompatImageView actionRedo = (AppCompatImageView) view.findViewById(R.id.redo);
        AppCompatImageView actionSaveDrawing = (AppCompatImageView) view.findViewById(R.id.complete_drawing);

        actionEraseAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBoard.eraseAll();
            }
        });

        actionUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBoard.undo();
            }
        });

        actionRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawBoard.redo();
            }
        });

        actionSaveDrawing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.addPaintElement(drawBoard.getDrawing());
                handler.setRegularLayout();
            }
        });
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
