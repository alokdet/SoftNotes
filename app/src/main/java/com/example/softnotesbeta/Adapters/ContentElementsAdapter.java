package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.net.Uri;
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
import com.example.softnotesbeta.Application.SoftScript.SoftScriptGenerator;
import com.example.softnotesbeta.Entities.ContentItem;
import com.example.softnotesbeta.Models.Item;
import com.example.softnotesbeta.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoProvider;

import java.util.List;

public class ContentElementsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ContentItem> items;

    public static final int VIEW_TYPE_IMAGE = 0;
    public static final int VIEW_TYPE_VIDEO = 1;
    public static final int VIEW_TYPE_AUDIO = 2;
    public static final int VIEW_TYPE_TEXT = 3;

    public ContentElementsAdapter(Context mContext, List<ContentItem> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_IMAGE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
                return new ImageViewHolder(view);
            case VIEW_TYPE_VIDEO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item, parent, false);
                return new VideoViewHolder(view);
            case VIEW_TYPE_AUDIO:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_item, parent,false);
                return new AudioViewHolder(view);
            case VIEW_TYPE_TEXT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
                return new TextViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ContentItem item = items.get(position);
        if (item != null) {
            String value = item.getValue();
            switch (item.getType()) {
                case SoftScriptGenerator.ELEMENT_IMAGE:
                    Picasso.get().load(Uri.parse(value)).placeholder(R.drawable.icon_image).error(R.drawable.icon_image).into(((ImageViewHolder)holder).imageView);
                    //Glide.with(mContext).load(item.getValue()).into(((ImageViewHolder)holder).imageView);
                    //((ImageViewHolder)holder).textView.setText(item.getUri());
                    //((ImageViewHolder)holder).textView2.setText(item.getType());
                    break;
                case SoftScriptGenerator.ELEMENT_VIDEO:
                    Picasso.get().load(Uri.parse(value)).into(((VideoViewHolder)holder).imageView);
                    //Glide.with(mContext).load(item.getUri()).into(((VideoViewHolder)holder).imageView);
                    //((VideoViewHolder)holder).textView.setText(item.getValue());
                    //((VideoViewHolder)holder).textView2.setText(item.getType());
                    break;
                case SoftScriptGenerator.ELEMENT_AUDIO:
                    ((AudioViewHolder)holder).textView.setText(item.getValue());
                    ((AudioViewHolder)holder).textView2.setText(item.getType());
                    break;
                case SoftScriptGenerator.ELEMENT_TEXT:
                    ((TextViewHolder)holder).textView.setText(item.getValue());
                    break;
            }
        }
    }

    public void submitList(List<ContentItem> list) {
        this.items = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        switch (items.get(position).getType()) {
            case SoftScriptGenerator.ELEMENT_IMAGE:
                return VIEW_TYPE_IMAGE;
            case SoftScriptGenerator.ELEMENT_VIDEO:
                return VIEW_TYPE_VIDEO;
            case SoftScriptGenerator.ELEMENT_AUDIO:
                return VIEW_TYPE_AUDIO;
            case SoftScriptGenerator.ELEMENT_TEXT:
                return VIEW_TYPE_TEXT;
            default:
                return position;
        }
    }

    public void handleItemInsertion() {
        int size = items.size();
        if (size > 1) {
            if (items.get(size - 2).getType().equals(SoftScriptGenerator.ELEMENT_TEXT)) {
                if (items.get(size - 2).getValue().equals(" ")) {
                    items.remove(size - 2);
                    notifyItemRemoved(size - 2);
                } else {

                }
            }
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        AppCompatTextView textView;
        AppCompatTextView textView2;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
            textView = (AppCompatTextView) itemView.findViewById(R.id.file_path);
            textView2 = (AppCompatTextView) itemView.findViewById(R.id.path_length);
        }
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        AppCompatTextView textView;
        AppCompatTextView textView2;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
            textView = (AppCompatTextView) itemView.findViewById(R.id.file_path);
            textView2 = (AppCompatTextView) itemView.findViewById(R.id.path_length);
        }
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView playButton;
        AppCompatTextView textView;
        AppCompatTextView textView2;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            playButton = (AppCompatImageView) itemView.findViewById(R.id.play_button);
            textView = (AppCompatTextView) itemView.findViewById(R.id.file_path);
            textView2 = (AppCompatTextView) itemView.findViewById(R.id.duration);
        }
    }

    public static class DocumentViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView imageView;
        AppCompatTextView textView;
        AppCompatTextView textView2;

        public DocumentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = (AppCompatImageView) itemView.findViewById(R.id.image_view);
            textView = (AppCompatTextView) itemView.findViewById(R.id.file_path);
            textView2 = (AppCompatTextView) itemView.findViewById(R.id.path_length);
        }
    }

    public static class TextViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView textView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (AppCompatTextView) itemView.findViewById(R.id.textview__);
        }
    }
}
