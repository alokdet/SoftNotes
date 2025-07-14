package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Entities.Task;
import com.example.softnotesbeta.OnTaskClickListener;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.TaskSelectionListener;

public class TaskAdapter extends PagedListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private Context context;
    private OnTaskClickListener listener;
    private TaskSelectionListener selectionListener;
    private boolean isSelected = false;

    public TaskAdapter(Context context, OnTaskClickListener listener, TaskSelectionListener selectionListener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.listener = listener;
        this.selectionListener = selectionListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item_layout, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position);

        if (task != null) {
            holder.titleTv.setText(task.getTitle());
            holder.taskDate.setText(task.getDateCreated());
            ViewCompat.setTransitionName(holder.itemView, String.valueOf(task.id));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onTaskClicked(task, holder.getAdapterPosition(), holder.itemView, holder.checkTask, isSelected);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isSelected = true;
                selectionListener.onTaskSelected(holder.getAdapterPosition(), task, holder.itemView, holder.checkTask, isSelected);
                return true;
            }
        });
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
        private AppCompatTextView taskDate;
        private AppCompatImageView checkTask;
       
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = (AppCompatTextView) itemView.findViewById(R.id.task_name);
            taskDate = (AppCompatTextView) itemView.findViewById(R.id.task_created_tv);
            checkTask = (AppCompatImageView) itemView.findViewById(R.id.check_task);
        }
    }



    public void setSelection(boolean selection) {
        this.isSelected = selection;
    }
}
