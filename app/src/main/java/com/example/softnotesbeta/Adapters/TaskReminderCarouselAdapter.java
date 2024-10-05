package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.OnTaskClickListener;
import com.example.softnotesbeta.R;

public class TaskReminderCarouselAdapter extends PagedListAdapter<Task, TaskReminderCarouselAdapter.TaskViewHolder> {

    private Context context;
    private OnTaskClickListener listener;

    public TaskReminderCarouselAdapter(Context context, OnTaskClickListener listener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_carousel_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position);

        if (task != null) {
            holder.titleTv.setText(task.getTitle());
            holder.detailsTv.setText(task.getDateCreated());
            holder.timerTv.setText(task.getDueDate());
        }
    }

    private static DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.id == newItem.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.equals(newItem);
        }
    };

    class TaskViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView titleTv;
        private AppCompatTextView detailsTv;
        private AppCompatTextView timerTv;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = (AppCompatTextView) itemView.findViewById(R.id.task_name);
            detailsTv = (AppCompatTextView) itemView.findViewById(R.id.details);
            timerTv = (AppCompatTextView) itemView.findViewById(R.id.time_tv);
        }
    }
}
