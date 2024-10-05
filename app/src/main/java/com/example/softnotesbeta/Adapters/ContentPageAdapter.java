package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Application.SoftScript.SoftScriptReader;
import com.example.softnotesbeta.Models.Item;
import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.R;

import java.util.ArrayList;
import java.util.List;

public class ContentPageAdapter extends RecyclerView.Adapter<ContentPageAdapter.ContentViewHolder> {

    private List<Step> stepsList;
    private Context context;
    private AppCompatTextView compatTextView;

    public ContentPageAdapter(List<Step> stepsList, Context context) {
        this.stepsList = stepsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_text_layout, parent, false);
        return new ContentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContentViewHolder holder, int position) {
        Step step = stepsList.get(position);
        String contentSoftScript = step.getContentSoftScript();

        holder.textView.setText(contentSoftScript);

        this.compatTextView = holder.textView;

        /*
        SoftScriptReader scriptReader = new SoftScriptReader();
        scriptReader.setScript(contentSoftScript);
        scriptReader.readScript();

        List<Item> itemList = new ArrayList<>();
        List<String> values = scriptReader.getValues();
        List<String> nodes = scriptReader.getNodes();

        for (int i = 0; i < values.size(); i++) {
            String path = values.get(i);
            Item item = new Item(path, nodes.get(i));
            itemList.add(item);
        }

        ContentElementsAdapter adapter = new ContentElementsAdapter(context, itemList);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerView.setAdapter(adapter);

         */

    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (AppCompatTextView) itemView.findViewById(R.id.content_text_tv);
        }
    }

    public AppCompatTextView getCurrentTextView() {
        return this.compatTextView;
    }
}
