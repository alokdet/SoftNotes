package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Entities.Step;
import com.example.softnotesbeta.R;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ImageViewHolder> {

    List<Step> steps;
    Context context;

    public StepsAdapter(List<com.example.softnotesbeta.Entities.Step> steps, Context context) {
        this.steps = steps;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.checkBox.setText(steps.get(position).getName());
        //holder.checkBox.setChecked(steps.get(position).isDone());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //steps.get(holder.getAdapterPosition()).setDone(isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox checkBox;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            checkBox = (AppCompatCheckBox) itemView.findViewById(R.id.check_step);
        }
    }
}
