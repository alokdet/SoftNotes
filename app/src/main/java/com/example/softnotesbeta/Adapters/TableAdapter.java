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
import com.example.softnotesbeta.TableItemRemovedListener;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableViewHolder> {

    private List<ListItem> indexList;
    private List<ListItem> keyList;
    private List<ListItem> valueList;
    private TableItemListener listener;
    private TableItemRemovedListener itemRemovedListener;
    private boolean showIndex = true;

    public TableAdapter(List<ListItem> indexList, List<ListItem> keyList, List<ListItem> valueList, TableItemListener listener, TableItemRemovedListener listener1) {
        this.indexList = indexList;
        this.keyList = keyList;
        this.valueList = valueList;
        this.listener = listener;
        this.itemRemovedListener = listener1;
    }

    public boolean isShowIndex() {
        return showIndex;
    }

    public void setShowIndex(boolean showIndex) {
        this.showIndex = showIndex;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_layout, parent, false);
        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, int position) {
        if (showIndex) {
            holder.displayIndex.setVisibility(View.VISIBLE);
            holder.displayIndex.setText(indexList.get(position).getText());
        } else {
            holder.displayIndex.setVisibility(View.GONE);
        }
        holder.displayKey.setText(keyList.get(position).getText());
        holder.displayValue.setText(valueList.get(position).getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(holder.getAdapterPosition(), keyList.get(holder.getAdapterPosition()).getText(), valueList.get(holder.getAdapterPosition()).getText());
            }
        });
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    class TableViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView displayIndex;
        AppCompatTextView displayKey;
        AppCompatTextView displayValue;
        public TableViewHolder(@NonNull View itemView) {
            super(itemView);

            displayIndex = (AppCompatTextView) itemView.findViewById(R.id.index);
            displayKey = (AppCompatTextView) itemView.findViewById(R.id.key);
            displayValue = (AppCompatTextView) itemView.findViewById(R.id.value);
        }
    }

    public void removeItem(int position) {
        itemRemovedListener.onItemRemoved(position, keyList.get(position).getText(), valueList.get(position).getText());
        indexList.remove(position);
        keyList.remove(position);
        valueList.remove(position);
        notifyItemRemoved(position);
        /*
        for (int i = position+1; i < indexList.size(); i++) {
            indexList.get(i).setText(String.valueOf(i-1));
            notifyItemChanged(i);
        }

         */
    }
}
