package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.softnotesbeta.Models.ImageItem;
import com.example.softnotesbeta.R;

import java.util.List;

public class ImageNoteAdapter extends RecyclerView.Adapter<ImageNoteAdapter.ImageViewHolder> {

    private Context mContext;
    private List<ImageItem> items;

    public ImageNoteAdapter(Context mContext, List<ImageItem> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_note_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ImageItem item = items.get(position);
        if (item != null) {
            Glide.with(mContext).load(item.getUri()).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
