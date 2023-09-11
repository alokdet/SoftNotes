package com.example.softnotesbeta;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;

import java.util.ArrayList;
import java.util.List;

public class TextEditor {

    public static final int FORMAT_BOLD = 0;
    public static final int FORMAT_ITALIC = 1;
    public static final int FORMAT_UNDERLINED = 2;
    public static final int FORMAT_STRIKETHROUGH = 3;

    private final List<Integer> currentFormats;
    private Context mContext;

    public TextEditor(Context context) {
        this.mContext = context;
        currentFormats = new ArrayList<>();
    }

    public void applyFormatting(Editable text, int start, int end, int formatCode) {
        if (formatCode == 0) {
            text.setSpan(new StyleSpan(Typeface.NORMAL), start, start + end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        for (int i = 0; i < currentFormats.size(); i++) {
            switch (i) {
                case FORMAT_BOLD:
                    text.setSpan(new StyleSpan(Typeface.BOLD), start, start + end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case FORMAT_ITALIC:
                    text.setSpan(new StyleSpan(Typeface.ITALIC), start, start + end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case FORMAT_UNDERLINED:
                    text.setSpan(new UnderlineSpan(), start, start + end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
                case FORMAT_STRIKETHROUGH:
                    text.setSpan(new StrikethroughSpan(), start, start + end, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                    break;
            }
        }
    }

    public void activate(int formatCode) {
        currentFormats.add(formatCode);
    }

    public void deActivate(int formatCode) {
        currentFormats.remove(formatCode);
    }
}
