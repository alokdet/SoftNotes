package com.example.softnotesbeta.Adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {



    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SettingsViewHolder extends RecyclerView.ViewHolder {

        public SettingsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
