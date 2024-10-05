package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Entities.Folder;
import com.example.softnotesbeta.Entities.Step;
import com.example.softnotesbeta.FolderCLickListener;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.StepCLickListener;

public class StepAdapter extends PagedListAdapter<Step, StepAdapter.StepViewHolder> {

    private Context context;
    private StepCLickListener listener;

    public StepAdapter(Context context, StepCLickListener listener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.create_step_item_layout, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        Step step = getItem(position);

        if (step != null) {
            holder.nameTv.setText(step.getName());

            //listener.onStepClicked(step.id, step.parentId, step.name);
        }
    }

    private static DiffUtil.ItemCallback<Step> DIFF_CALLBACK = new DiffUtil.ItemCallback<Step>() {
        @Override
        public boolean areItemsTheSame(@NonNull Step oldItem, @NonNull Step newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Step oldItem, @NonNull Step newItem) {
            return oldItem.equals(newItem);
        }
    };

    class StepViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView nameTv;


        public StepViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTv = (AppCompatTextView) itemView.findViewById(R.id.step_text);
        }
    }
}
