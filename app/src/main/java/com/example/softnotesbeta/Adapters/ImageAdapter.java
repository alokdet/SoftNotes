package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.softnotesbeta.ImageItemClickListener;
import com.example.softnotesbeta.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    List<String> imagePaths;
    ImageItemClickListener clickListener;
    Context context;

    public ImageAdapter(List<String> imagePaths, ImageItemClickListener clickListener, Context context) {
        this.imagePaths = imagePaths;
        this.clickListener = clickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item_layout, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        Glide.with(context).load(imagePaths.get(position)).into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onImageClick(imagePaths.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
        }
    }
}
