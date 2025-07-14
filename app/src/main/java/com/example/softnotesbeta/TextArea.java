package com.example.softnotesbeta;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatEditText;

public class TextArea extends AppCompatEditText {
    public static final int TYPEFACE_NORMAL = 0;
    public static final int TYPEFACE_BOLD = 1;
    public static final int TYPEFACE_ITALICS = 2;
    public static final int TYPEFACE_BOLD_ITALICS = 3;

    private int currentTypeface;
    private int lastCursorPosition;
    private int tId;


    public TextArea(Context context) {
        super(context);
        lastCursorPosition = this.getSelectionStart();
    }

    public TextArea(Context context, AttributeSet attrs) {
        super(context, attrs);
        lastCursorPosition = this.getSelectionStart();
    }


    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public void changeTypeface(int tfId) {
        currentTypeface = tfId;
        lastCursorPosition = this.getSelectionStart();
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        Spannable str = this.getText();
        StyleSpan ss;
        int endLength = text.toString().length();

        switch(currentTypeface) {
            case TYPEFACE_NORMAL:
                ss = new StyleSpan(Typeface.NORMAL);
                break;
            case TYPEFACE_BOLD:
                ss = new StyleSpan(Typeface.BOLD);
                break;
            case TYPEFACE_ITALICS:
                ss = new StyleSpan(Typeface.ITALIC);
                break;
            case TYPEFACE_BOLD_ITALICS:
                ss = new StyleSpan(Typeface.BOLD_ITALIC);
                break;
            default:
                ss = new StyleSpan(Typeface.NORMAL);
        }

        str.setSpan(ss, lastCursorPosition, endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
}
