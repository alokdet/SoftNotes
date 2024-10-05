package com.example.softnotesbeta.WorkspaceControllers;

import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Adapters.ImageNoteAdapter;
import com.example.softnotesbeta.Application.SoftScript.SoftScriptGenerator;
import com.example.softnotesbeta.Application.SoftScript.SoftScriptReader;
import com.example.softnotesbeta.Models.ImageItem;
import com.example.softnotesbeta.R;

import java.util.ArrayList;
import java.util.List;

public class ImageController {

    private String softScript;
    private ConstraintLayout layout;
    private View view;
    private NoteControllerHandler handler;
    private SoftScriptGenerator generator;
    private SoftScriptReader reader;
    private RecyclerView imageSpace;
    private AppCompatTextView dateTimeTv;
    private List<ImageItem> itemList = new ArrayList<>();
    private ImageNoteAdapter adapter;

    public ImageController(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.image_note_layout, layout, false);
        handler = NoteControllerHandler.getInstance();
        dateTimeTv = view.findViewById(R.id.date_time_tv);
        imageSpace = (RecyclerView) view.findViewById(R.id.image_space);
        generator = new SoftScriptGenerator();
        reader = new SoftScriptReader();
        adapter = new ImageNoteAdapter(layout.getContext(), itemList);
        imageSpace.setLayoutManager(new LinearLayoutManager(layout.getContext()));
        imageSpace.setAdapter(adapter);
    }

    public void activate() {
        layout.removeAllViews();
        layout.addView(view);
    }

    public void displayImages() {

        reader.setScript(softScript);
        reader.readScript();

        List<String> values = reader.getValues();
        List<String> nodes = reader.getNodes();

        for (int i = 0; i < values.size(); i++) {
            String path = values.get(i);
            ImageItem item = new ImageItem(path);
            itemList.add(item);
            adapter.notifyDataSetChanged();
        }
    }

    public void setSoftScript(String softScript) {
        this.softScript = softScript;
    }

    public String getSoftScript() {
        return this.softScript;
    }

    public String getGeneratedSoftScript() {
        return generator.getScript() + getSoftScript();
    }

    public String getDate() {
        return dateTimeTv.getText().toString();
    }

    public void setDate(String date) {
        dateTimeTv.setText(date);
    }

    public void handleImageInsertion(String path) {
        generator.addRow(SoftScriptGenerator.ELEMENT_IMAGE, path);
        itemList.add(new ImageItem(path));
        adapter.notifyDataSetChanged();
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
