package com.example.softnotesbeta.WorkspaceControllers;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.softnotesbeta.Adapters.Pagination;
import com.example.softnotesbeta.Adapters.TextPagerAdapter;
import com.example.softnotesbeta.Application.Common;
import com.example.softnotesbeta.Database.DatabaseManager;
import com.example.softnotesbeta.R;

public class WriteController {

    private ConstraintLayout layout;
    private View view;
    private final AppCompatEditText inputNote;
    private final AppCompatTextView dateView;
    private final AppCompatTextView pageNumberView;
    private ViewPager pager;
    private ScrollView textScroller;
    private String note;
    private String date;
    private NoteControllerHandler handler;
    private Pagination pagination;
    private int currentIndex = 0;
    private int currentMode = 0;

    public WriteController(ConstraintLayout layout) {
        this.layout = layout;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.note_writer_layout, layout, false);
        inputNote = (AppCompatEditText) view.findViewById(R.id.input_note);
        dateView = (AppCompatTextView) view.findViewById(R.id.date_time_tv);
        pageNumberView = (AppCompatTextView) view.findViewById(R.id.page_number_tv);
        pager = (ViewPager) view.findViewById(R.id.text_pager);
        textScroller = (ScrollView) view.findViewById(R.id.note_scroll_view);

        handler = NoteControllerHandler.getInstance();

        inputNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String charCount = String.valueOf(s.length());
                String lineCount = String.valueOf(inputNote.getLineCount());
                String textToDisplay = charCount + " chars \n" + lineCount + " lines";
                handler.setNoteDetails(textToDisplay);
            }
        });

        inputNote.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                inputNote.getViewTreeObserver().removeOnGlobalLayoutListener(this::onGlobalLayout);
                pagination = new Pagination(
                        inputNote.getIncludeFontPadding(),
                        inputNote.getWidth(),
                        inputNote.getLineHeight() * 20,
                        inputNote.getLineSpacingMultiplier(),
                        inputNote.getLineSpacingExtra(),
                        inputNote.getText(),
                        inputNote.getPaint()
                );
            }
        });

        inputNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    addMargins();
                    handler.revealActions();
                } else {
                    removeMargins();
                    handler.hideActions();
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (currentMode == 1) {
                    pageNumberView.setText(String.valueOf(position + 1) + "/" + pager.getAdapter().getCount());
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void removeMargins() {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                params.bottomMargin = 0;
                layout.setLayoutParams(params);
            }
        };
        animation.setDuration(500);
        layout.startAnimation(animation);
    }

    private void addMargins() {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) layout.getLayoutParams();

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                params.bottomMargin = (int) (dpToPixels(68) * interpolatedTime);
                layout.setLayoutParams(params);
            }
        };
        animation.setDuration(500);
        layout.startAnimation(animation);
    }

    private int dpToPixels(int dp) {
        float density = layout.getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public void setAlignment(int alignment) {
        inputNote.setTextAlignment(alignment);
        Spannable spannable = new SpannableStringBuilder(inputNote.getText());
        inputNote.setText(spannable);
    }

    public int getSelectionStart() {
        return inputNote.getSelectionStart();
    }

    public int getSelectionEnd() {
        return inputNote.getSelectionEnd();
    }

    public int getAlignment() {
        return inputNote.getTextAlignment();
    }

    public void changeMode() {
        if (currentMode == 0) {
            currentMode = 1;
        } else if (currentMode == 1) {
            currentMode = 0;
        } else {
            currentMode = 0;
        }
        handlePage();
    }

    public void handlePage() {
        switch (currentMode) {
            case 0:
                if (pager.getVisibility() == View.VISIBLE) {
                    pager.setVisibility(View.GONE);
                    pageNumberView.setVisibility(View.GONE);
                    textScroller.setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                if (pager.getVisibility() == View.GONE) {
                    pager.setVisibility(View.VISIBLE);
                    pageNumberView.setVisibility(View.VISIBLE);
                    textScroller.setVisibility(View.GONE);
                    paginateText();
                }
                break;
        }
    }

    private void handleScrollableText() {

    }

    private void paginateText() {
        TextPagerAdapter adapter = new TextPagerAdapter(layout.getContext(), pagination.getPages());
        pager.setAdapter(adapter);
    }

    public String getNote() {
        note = inputNote.getText().toString();
        return note;
    }

    public void setNote(String note) {
        this.note = note;
        inputNote.setText(note);
        Common.text = note;
    }

    public void setNote(SpannableStringBuilder note) {
        this.note = note.toString();
        inputNote.setText(note);
        Common.text = note.toString();
    }

    public void setNote(Spannable note) {
        this.note = note.toString();
        inputNote.setText(note);
        Common.text = note.toString();
    }

    public String getDate() {
        date = dateView.getText().toString();
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
        dateView.setText(date);
    }

    public void activate() {
        layout.addView(view);
    }

    public void deActivate() {
        layout.removeView(view);
    }
}
