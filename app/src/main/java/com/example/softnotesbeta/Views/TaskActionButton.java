package com.example.softnotesbeta.Views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TaskActionButton extends ConstraintLayout {

    private enum State {
        EXPAND,
        COLLAPSE
    }

    public TaskActionButton(@NonNull Context context) {
        super(context);
        init(context);
    }

    public TaskActionButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TaskActionButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

    }
}
