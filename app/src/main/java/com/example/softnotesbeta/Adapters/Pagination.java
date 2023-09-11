package com.example.softnotesbeta.Adapters;

import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import java.util.ArrayList;
import java.util.List;

public class Pagination {
    private final boolean mIncludePad;
    private final int mWidth;
    private final int mHeight;
    private final float mSpacingMult;
    private final float mSpacingAdd;
    private final CharSequence mText;
    private final TextPaint mPaint;
    private final List<CharSequence> mPages;

    public Pagination(boolean mIncludePad, int mWidth, int mHeight, float mSpacingMult, float mSpacingAdd, CharSequence mText, TextPaint mPaint) {
        this.mIncludePad = mIncludePad;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.mSpacingMult = mSpacingMult;
        this.mSpacingAdd = mSpacingAdd;
        this.mText = mText;
        this.mPaint = mPaint;
        this.mPages = new ArrayList<>();
        layout();
    }

    private void layout() {
        final StaticLayout layout = new StaticLayout(mText, mPaint, mWidth, Layout.Alignment.ALIGN_NORMAL,mSpacingMult, mSpacingAdd, mIncludePad);
        final int lines = layout.getLineCount();
        final CharSequence text = layout.getText();
        int startOffset = 0;
        int height = mHeight;

        for (int i = 0; i < lines; i++) {
            if (height < layout.getLineBottom(i)) {
                addPage(text.subSequence(startOffset, layout.getLineStart(i)));
                startOffset = layout.getLineStart(i);
                height = layout.getLineTop(i) + mHeight;
            }
            if (i == lines - 1) {
                addPage(text.subSequence(startOffset, layout.getLineEnd(i)));
                return;
            }
        }
    }

    private void addPage(CharSequence text) {
        mPages.add(text);
    }

    public int size() {
        return mPages.size();
    }

    public CharSequence get(int index) {
        return (index >= 0 && index <mPages.size()) ? mPages.get(index) : null;
    }

    public List<CharSequence> getPages() {
        return mPages;
    }
}
