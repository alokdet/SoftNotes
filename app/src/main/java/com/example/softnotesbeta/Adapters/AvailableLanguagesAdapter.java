package com.example.softnotesbeta.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.softnotesbeta.Models.TranslateTargetModel;
import com.example.softnotesbeta.R;

public class AvailableLanguagesAdapter extends RecyclerView.Adapter<AvailableLanguagesAdapter.LanguageViewHolder> {

    private String[] languages;

    public AvailableLanguagesAdapter(String[] languages) {
        this.languages = languages;
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.language_layout, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        holder.language.setText(languages[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TranslateTargetModel.getInstance().changeTargetLanguage(languages[holder.getAdapterPosition()]);
            }
        });

        
    }

    @Override
    public int getItemCount() {
        return languages.length;
    }

    class LanguageViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView language;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);

            language = (AppCompatTextView) itemView.findViewById(R.id.language);
        }
    }
}
