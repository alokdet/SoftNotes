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

public class TaskAdapter extends PagedListAdapter<Task, TaskAdapter.TaskViewHolder> {

    private Context context;
    private OnTaskClickListener listener;

    public TaskAdapter(Context context, OnTaskClickListener listener) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.listener = listener;
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
            boolean expanded = task.isExpanded();
            holder.subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = task.isExpanded();
                task.setExpanded(!expanded);
                notifyItemChanged(holder.getAdapterPosition());

                holder.stepView.setLayoutManager(new LinearLayoutManager(context));
                holder.stepView.setAdapter(new StepsAdapter(task.getSteps(), context));

                holder.checkTask.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onTaskClicked(task);
                    }
                });
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
        private ConstraintLayout subItem;
        private AppCompatImageView checkTask;
        private RecyclerView stepView;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTv = (AppCompatTextView) itemView.findViewById(R.id.task_name);
            subItem = (ConstraintLayout) itemView.findViewById(R.id.task_details_layout);
            checkTask = (AppCompatImageView) itemView.findViewById(R.id.check_task);
            stepView = (RecyclerView) itemView.findViewById(R.id.steps_view);
        }
    }
}
