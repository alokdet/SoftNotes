package com.example.softnotesbeta.PlannerControllers;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.DAOs.NoteDao;
import com.example.softnotesbeta.DAOs.PreviewDao;
import com.example.softnotesbeta.Database.NotesDatabase;
import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.Models.SaveNoteModel;
import com.example.softnotesbeta.PlannerControllers.PlannerActions.PlannerActionsHandler;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.Views.BoardImageView;
import com.example.softnotesbeta.WorkspaceControllers.NoteControllerHandler;

import java.util.ArrayList;
import java.util.List;

public class RegularLayout {

    private ConstraintLayout layout;
    private ConstraintLayout elementGroup;
    private ConstraintLayout actionsGroup;
    private View view;
    private AppCompatImageView actionAddElement;
    private AppCompatImageView performAction;
    private RecyclerView recyclerView;
    private AppCompatEditText inputCommands;
    private LinearLayoutCompat viewGroup;

    private MindMapHandler handler;
    private PlannerActionsHandler actionsHandler;
    private NotesDatabase database;
    private NoteDao noteDao;
    private PreviewDao previewDao;

    public RegularLayout(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.regular_mind_map_layout, layout, false);
        actionAddElement = (AppCompatImageView) view.findViewById(R.id.add_element);
        performAction = (AppCompatImageView) view.findViewById(R.id.perform_action);
        recyclerView = (RecyclerView) view.findViewById(R.id.elements_list);
        elementGroup = (ConstraintLayout) view.findViewById(R.id.elements_container);
        inputCommands = (AppCompatEditText) view.findViewById(R.id.input_commands);
        viewGroup = (LinearLayoutCompat) view.findViewById(R.id.view_group_to_test);

        actionsHandler = PlannerActionsHandler.getInstance();
        //actionsHandler.initialise(actionsGroup);

        handler = MindMapHandler.getInstance();
        database = NotesDatabase.getInstance(layout.getContext());
        noteDao = database.noteDao();
        previewDao = database.previewDao();
    }

    public void activate() {
        layout.addView(view);
        handler.setCurrentLayout(0);

        recyclerView.setLayoutManager(new LinearLayoutManager(layout.getContext(), RecyclerView.HORIZONTAL, false));

        actionAddElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.setPaintLayout();
            }
        });

        performAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String command = inputCommands.getText().toString();

                readCommand(command);
            }
        });
    }

    private void readCommand(String command) {
        String word = "";
        List<String> words = new ArrayList<>();

        for (int i = 0; i < command.length(); i++) {
            char charI = command.charAt(i);

            if (charI == '_') {
                words.add(word);
                word = "";
            } else {
                word = word + String.valueOf(charI);
            }
        }
        showView(words);
    }

    private void showView(List<String> words) {
        for (int i = 0; i < words.size(); i++) {
            String word = words.get(i);
            displayView(word);
        }
    }

    private void displayView(String word) {
        switch (word) {
            case "BUTTON":
                Button button = new Button(viewGroup.getContext());
                viewGroup.addView(button);
                break;
            case "SWITCH":
                SwitchCompat switchView = new SwitchCompat(viewGroup.getContext());
                viewGroup.addView(switchView);
                break;
            case "CHECKBOX":
                CheckBox checkBox = new CheckBox(viewGroup.getContext());
                viewGroup.addView(checkBox);
                break;
        }
    }

    public void addElement(Bitmap bitmap) {
        BoardImageView imageView = new BoardImageView(layout.getContext());
        imageView.setImageBitmap(resize(bitmap, 360, 840));

        elementGroup.addView(imageView);
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float bitmapRatio = (float) width / (float) height;
            float maxRatio = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (maxRatio > bitmapRatio) {
                finalWidth = (int) ((float) maxHeight * bitmapRatio);
            } else {
                finalHeight = (int) ((float) maxWidth / bitmapRatio);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    public void deActivate() {
        layout.removeView(view);
    }

    private class ElementsAdapter extends RecyclerView.Adapter<ElementsAdapter.ElementViewHolder> {

        @NonNull
        @Override
        public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_list_item, parent, false);
            return new ElementViewHolder(view1);
        }

        @Override
        public void onBindViewHolder(@NonNull ElementViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ElementViewHolder extends RecyclerView.ViewHolder {
            AppCompatImageView imageView;

            public ElementViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = (AppCompatImageView) itemView.findViewById(R.id.preview);
            }
        }
    }
}
