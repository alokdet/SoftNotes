package com.example.softnotesbeta.Adapters;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.ConfigureStepContentListener;
import com.example.softnotesbeta.ContentActivity;

import com.example.softnotesbeta.Models.Step;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.StepCLickListener;
import com.example.softnotesbeta.StepDeletedListener;
import com.example.softnotesbeta.StepUpdateRequest;

import java.util.List;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepViewHolder> {

    public static final int FRAGMENT_CREATE_TASK = 0;
    public static final int FRAGMENT_VIEW_TASK = 1;
    private List<Step> stepsList;
    private Context context;
    private StepCLickListener listener;
    private StepDeletedListener stepDeletedListener;
    private StepUpdateRequest updateRequest;
    private int fragment;

    public StepsListAdapter(List<Step> stepsList, Context context, StepCLickListener listener) {
        this.stepsList = stepsList;
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
        ViewCompat.setTransitionName(holder.itemView, String.valueOf(System.currentTimeMillis()));
        holder.stepText.setText(stepsList.get(position).getName());

        if (fragment == FRAGMENT_VIEW_TASK) {
            holder.createContent.setVisibility(View.GONE);
        }

        holder.createContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == FRAGMENT_CREATE_TASK) {
                    listener.onStepClicked(stepsList.get(holder.getAdapterPosition()), holder.itemView, holder.getAdapterPosition());
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fragment == FRAGMENT_CREATE_TASK) {
                    updateRequest.onUpdateRequested(holder.getAdapterPosition(), stepsList.get(holder.getAdapterPosition()).getName());
                } else {
                    listener.onStepClicked(stepsList.get(holder.getAdapterPosition()), holder.itemView, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return stepsList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView stepText;
        AppCompatImageView createContent;

        public StepViewHolder(@NonNull View itemView) {
            super(itemView);

            stepText = (AppCompatTextView) itemView.findViewById(R.id.step_text);
            createContent = (AppCompatImageView) itemView.findViewById(R.id.define_step);
        }
    }

    public StepDeletedListener getStepDeletedListener() {
        return stepDeletedListener;
    }

    public void setStepDeletedListener(StepDeletedListener stepDeletedListener) {
        this.stepDeletedListener = stepDeletedListener;
    }

    public StepUpdateRequest getUpdateRequest() {
        return updateRequest;
    }

    public void setUpdateRequest(StepUpdateRequest updateRequest) {
        this.updateRequest = updateRequest;
    }

    public int getFragment() {
        return fragment;
    }

    public void setFragment(int fragment) {
        this.fragment = fragment;
    }

    public void removeItem(int position) {
        stepDeletedListener.onStepDeleted(position);
    }
}
