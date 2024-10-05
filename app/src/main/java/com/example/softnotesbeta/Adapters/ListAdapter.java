package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.ListItemListener;
import com.example.softnotesbeta.ListItemRemovedListener;
import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.R;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListItemViewHolder> {

    private List<ListItem> itemsList;
    private Context mContext;
    private ListItemRemovedListener listItemRemovedListener;
    private ListItemListener listener;
    private boolean compose;

    public ListAdapter(List<ListItem> itemsList, Context mContext, ListItemRemovedListener listItemRemovedListener, ListItemListener listener) {
        this.itemsList = itemsList;
        this.mContext = mContext;
        this.listItemRemovedListener = listItemRemovedListener;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemViewHolder holder, int position) {
        holder.checkItem.setText(itemsList.get(position).getText());
        holder.checkItem.setChecked(itemsList.get(position).isChecked());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.getAdapterPosition(), itemsList.get(holder.getAdapterPosition()).getText(), holder.checkItem.isChecked());
            }
        });

        holder.checkItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (compose) {
                        holder.checkItem.setChecked(false);
                    } else {
                        itemsList.get(holder.getAdapterPosition()).setChecked(b);
                    }
                }
                listener.onClick(holder.getAdapterPosition(), itemsList.get(holder.getAdapterPosition()).getText(), holder.checkItem.isChecked());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class ListItemViewHolder extends RecyclerView.ViewHolder {

        AppCompatCheckBox checkItem;

        public ListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            checkItem = (AppCompatCheckBox) itemView.findViewById(R.id.check_item);
        }
    }

    public void removeItem(int position) {
        listItemRemovedListener.onItemRemoved(position, itemsList.get(position).getText());
        itemsList.remove(position);
        notifyItemRemoved(position);
    }

    public void setCompose(boolean compose) {
        this.compose = compose;
    }
}
