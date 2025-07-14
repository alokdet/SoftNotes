package com.example.softnotesbeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.TableItemListener;
import com.example.softnotesbeta.TimelineItemListener;
import com.example.softnotesbeta.TimelineItemRemovedListener;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder> {
    private List<ListItem> textList;
    private List<ListItem> timeList;
    private TimelineItemListener listener;
    private TimelineItemRemovedListener removedListener;

    public TimelineAdapter(List<ListItem> textList, List<ListItem> timeList, TimelineItemListener listener, TimelineItemRemovedListener removedListener) {
        this.textList = textList;
        this.timeList = timeList;
        this.listener = listener;
        this.removedListener = removedListener;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_layout, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineViewHolder holder, int position) {
        holder.displayTime.setText(timeList.get(position).getStringText());
        holder.displayText.setText(textList.get(position).getStringText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.getAdapterPosition(), timeList.get(holder.getAdapterPosition()).getText(), textList.get(holder.getAdapterPosition()).getText());
            }
        });

        holder.displayText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.getAdapterPosition(), timeList.get(holder.getAdapterPosition()).getText(), textList.get(holder.getAdapterPosition()).getText());
            }
        });

        holder.displayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.getAdapterPosition(), timeList.get(holder.getAdapterPosition()).getText(), textList.get(holder.getAdapterPosition()).getText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeList.size();
    }

    class TimelineViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView displayTime;
        AppCompatTextView displayText;
        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);

            displayText = (AppCompatTextView) itemView.findViewById(R.id.text);
            displayTime = (AppCompatTextView) itemView.findViewById(R.id.time);
        }
    }

    public void removeItem(int position) {
        removedListener.onItemRemoved(position, timeList.get(position).getText(), textList.get(position).getText());
        textList.remove(position);
        timeList.remove(position);
        notifyItemRemoved(position);
    }
}
