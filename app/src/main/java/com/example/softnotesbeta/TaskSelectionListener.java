package com.example.softnotesbeta;

import android.view.View;

import com.example.softnotesbeta.Entities.Task;

public interface TaskSelectionListener {
    void onTaskSelected(int position, Task task, View itemView, View checkView, boolean isSelected);
}
