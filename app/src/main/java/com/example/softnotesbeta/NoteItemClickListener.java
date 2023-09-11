package com.example.softnotesbeta;

import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.example.softnotesbeta.Entities.Note;
import com.example.softnotesbeta.Entities.Preview;

public interface NoteItemClickListener {

    void onPreviewCLick(int position, Preview preview, View view1, View view2, boolean isSelected);
}
