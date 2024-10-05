package com.example.softnotesbeta.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.R;

import java.util.List;

public class TranslatePagerAdapter extends RecyclerView.Adapter<TranslatePagerAdapter.PageViewHolder> {

    private List<String> pages;

    public TranslatePagerAdapter(List<String> pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_page_layout, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        holder.editText.setText(pages.get(position));

        if (position == 0) {
            holder.editText.setTextColor(Color.YELLOW);
        }

    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    class PageViewHolder extends RecyclerView.ViewHolder {

        AppCompatEditText editText;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);

            editText = (AppCompatEditText) itemView.findViewById(R.id.writer_input);
        }
    }
}
