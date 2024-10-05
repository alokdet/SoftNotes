package com.example.softnotesbeta.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BulletSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;
import com.example.softnotesbeta.MainActivity;
import com.example.softnotesbeta.Models.ListItem;
import com.example.softnotesbeta.NoteItemClickListener;

import com.example.softnotesbeta.OnSelectionStart;
import com.example.softnotesbeta.R;

import java.util.List;

public class NoteAdapter extends PagedListAdapter<Preview, NoteAdapter.NoteViewHolder> {

    private Context context;
    private NoteItemClickListener clickListener;
    private OnSelectionStart selectionStart;

    private boolean isSelected = false;

    public NoteAdapter(Context context, NoteItemClickListener clickListener, OnSelectionStart selectionStart) {
        super(DIFF_CALLBACK);

        this.context = context;
        this.clickListener = clickListener;
        this.selectionStart = selectionStart;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item_layout, viewGroup, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i) {
        Preview note = getItem(i);

        if (note != null) {
            noteViewHolder.titleTextView.setText(note.title);

            if (note.getType().equals("list")) {
                noteViewHolder.previewTextView.setText("List");
            } else {
                noteViewHolder.previewTextView.setText(note.preview);
            }
            noteViewHolder.dateTextView.setText(note.date);

            ViewCompat.setTransitionName(noteViewHolder.item_layout, String.valueOf(note.id));
        }


        noteViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                clickListener.onPreviewCLick(noteViewHolder.getAdapterPosition(), note, noteViewHolder.item_layout, noteViewHolder.select_icon, isSelected);
                /*
                noteViewHolder.select_icon.setVisibility(View.VISIBLE);

                Drawable drawable = noteViewHolder.item_layout.getBackground();
                drawable = DrawableCompat.wrap(drawable);

                DrawableCompat.setTint(drawable, context.getResources().getColor(R.color.selected_color));
                noteViewHolder.item_layout.setBackground(drawable);

                 */
            }
        });

        noteViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isSelected = !isSelected;
                selectionStart.performActions(isSelected);
                clickListener.onPreviewCLick(noteViewHolder.getAdapterPosition(), note, noteViewHolder.item_layout, noteViewHolder.select_icon, isSelected);
                return true;
            }
        });
    }

    private static DiffUtil.ItemCallback<Preview> DIFF_CALLBACK = new DiffUtil.ItemCallback<Preview>() {
        @Override
        public boolean areItemsTheSame(@NonNull Preview note, @NonNull Preview t1) {
            return note.id == t1.id;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Preview note, @NonNull Preview t1) {
            return note.equals(t1);
        }
    };

    private float dp(int dp) {
        return context.getResources().getDisplayMetrics().density * dp;
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{

        AppCompatTextView titleTextView, previewTextView;
        AppCompatTextView dateTextView;
        AppCompatImageView select_icon;
        ConstraintLayout item_layout;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = (AppCompatTextView) itemView.findViewById(R.id.tv_title);
            previewTextView = (AppCompatTextView) itemView.findViewById(R.id.tv_preview);
            dateTextView = (AppCompatTextView) itemView.findViewById(R.id.tv_date);
            select_icon = (AppCompatImageView) itemView.findViewById(R.id.select_indic);
            item_layout = (ConstraintLayout) itemView.findViewById(R.id.item_container);
        }
    }

    private CharSequence makeListPreview(List<ListItem> list) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        for (int index = 0; index < list.size();  index++) {
            String text = list.get(index).getText();
            String line = text + (index < list.size() - 1 ? "\n" : "");

            Spannable spannable = new SpannableString(line);
            spannable.setSpan(new BulletSpan(15, Color.GRAY), 0, spannable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(spannable);
        }
        return spannableStringBuilder;
    }
}
