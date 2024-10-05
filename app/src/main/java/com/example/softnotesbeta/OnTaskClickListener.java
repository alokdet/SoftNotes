package com.example.softnotesbeta;

import android.view.View;

import com.example.softnotesbeta.Entities.Task;

public interface OnTaskClickListener {
    void onTaskClicked(Task task, int position, View itemView, View checkView, boolean isSelected);
}
