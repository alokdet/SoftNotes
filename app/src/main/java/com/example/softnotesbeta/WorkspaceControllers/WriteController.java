package com.example.softnotesbeta.WorkspaceControllers;

import android.app.Activity;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ScrollView;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.softnotesbeta.Adapters.Pagination;
import com.example.softnotesbeta.Adapters.TextPagerAdapter;
import com.example.softnotesbeta.Adapters.TranslatePagerAdapter;
import com.example.softnotesbeta.Models.TranslateCallbacks;
import com.example.softnotesbeta.Models.TranslateTargetModel;
import com.example.softnotesbeta.R;
import com.example.softnotesbeta.TextTranslator;
import com.example.softnotesbeta.Workspace;

import java.util.ArrayList;
import java.util.List;

public class WriteController implements TranslateCallbacks.TranslateText, TranslateTargetModel.TargetListener, TextToSpeech.OnInitListener {

    private ConstraintLayout layout;
    private View view;
    private Workspace activity;
    private final AppCompatEditText inputNote;
    private final AppCompatTextView dateView;
    private final AppCompatTextView translationIndic;
    private final AppCompatTextView ttsView;
    private final AppCompatTextView pageNumberView;
    private ViewPager pager;
    private ViewPager2 translatePager;
    private ScrollView textScroller;
    private String note;
    private String date;
    private NoteControllerHandler handler;
    private TranslatePagerAdapter adapter;
    private List<String> translatePages = new ArrayList<>();
    private Pagination pagination;
    private int currentIndex = 0;
    private int currentMode = 0;
    private String targetLanguage;
    private String translatedText;
    private TextToSpeech textToSpeech;
    private boolean header = false;

    public WriteController(ConstraintLayout layout, Workspace activity) {
        this.layout = layout;
        this.activity = activity;
        view = LayoutInflater.from(layout.getContext()).inflate(R.layout.note_writer_layout, layout, false);
        inputNote = (AppCompatEditText) view.findViewById(R.id.input_note);
        dateView = (AppCompatTextView) view.findViewById(R.id.date_time_tv);
        pageNumberView = (AppCompatTextView) view.findViewById(R.id.page_number_tv);
        translationIndic = (AppCompatTextView) view.findViewById(R.id.translation_indic);
        ttsView = (AppCompatTextView) view.findViewById(R.id.text_for_tts);
        pager = (ViewPager) view.findViewById(R.id.text_pager);
        translatePager = (ViewPager2) view.findViewById(R.id.translate_pager);
        textScroller = (ScrollView) view.findViewById(R.id.note_scroll_view);

        adapter = new TranslatePagerAdapter(translatePages);

        handler = NoteControllerHandler.getInstance();

        TranslateCallbacks.getInstance().setListener(this);
        TranslateTargetModel.getInstance().setListener(this);

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

                if (header) {
                    Spannable spannable = (Spannable) s;
                    spannable.setSpan(new AbsoluteSizeSpan(30, true), (s.length() - 1), s.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
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
                    //addMargins();
                    //handler.revealActions();
                } else {
                    //removeMargins();
                    //handler.hideActions();
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

    public void changeMode(int mode) {
        currentMode = mode;
        handlePage(mode);
    }

    public void handlePage(int mode) {
        switch (currentMode) {
            case 0:
                if (textScroller.getVisibility() == View.GONE) {
                    pager.setVisibility(View.GONE);
                    pageNumberView.setVisibility(View.GONE);
                    translatePager.setVisibility(View.GONE);
                    textScroller.setVisibility(View.VISIBLE);
                    dateView.setVisibility(View.VISIBLE);
                    translationIndic.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (pager.getVisibility() == View.GONE) {
                    pager.setVisibility(View.VISIBLE);
                    pageNumberView.setVisibility(View.VISIBLE);
                    textScroller.setVisibility(View.GONE);
                    translatePager.setVisibility(View.GONE);
                    dateView.setVisibility(View.VISIBLE);
                    translationIndic.setVisibility(View.GONE);
                    paginateText();
                }
            case 2:
                if (translatePager.getVisibility() == View.GONE) {
                    translatePager.setVisibility(View.VISIBLE);
                    pager.setVisibility(View.GONE);
                    pageNumberView.setVisibility(View.GONE);
                    textScroller.setVisibility(View.GONE);
                    dateView.setVisibility(View.GONE);
                    translationIndic.setVisibility(View.VISIBLE);
                    setUpAdapter();
                }
                break;
        }
    }

    public void startTTS() {

        if (textScroller.getVisibility() == View.VISIBLE) {
            inputNote.setVisibility(View.GONE);
            ttsView.setVisibility(View.VISIBLE);

            ttsView.setText(getNote());

            textToSpeech = new TextToSpeech(activity.getApplicationContext(), this);
            textToSpeech.speak(getNote(), TextToSpeech.QUEUE_FLUSH, null, "Doesn't matter yet");
        }
    }

    private void setUpAdapter() {
        translatePages.add(getNote());
        translatePager.setAdapter(adapter);

        dateView.setGravity(Gravity.CENTER);

        translatePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                switch (position) {
                    case 0:
                        translationIndic.setText("Original Text");
                        break;
                    case 1:
                        translationIndic.setText("Translated Text");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
    }

    private void handleScrollableText() {

    }

    public void setHeader(boolean mode) {
        this.header = mode;
    }

    public boolean getHeader() {
        return this.header;
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
    }

    public void setNote(SpannableStringBuilder note) {
        this.note = note.toString();
        inputNote.setText(note);
    }

    public void setNote(Spannable note) {
        this.note = note.toString();
        inputNote.setText(note);
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
        layout.removeAllViews();
        layout.addView(view);
    }

    public void deActivate() {
        layout.removeView(view);
    }

    @Override
    public void onTranslate() {
        if (TextUtils.isEmpty(translatedText)) {
            setNote(inputNote.getText().toString());
        } else {
            setNote(translatedText);
        }
        handler.normalTextMode();
    }

    private void setTargetLanguage(String language) {
        this.targetLanguage = language;
    }

    public void setTranslatedText(String text) {
        if (translatePages.size() > 1) {
            translatePages.remove(translatePages.size() - 1);
            translatePages.add(text);
        } else {
            translatePages.add(text);
        }
        adapter.notifyDataSetChanged();
        this.translatedText = text;
    }

    @Override
    public void onTargetChanged(String language) {
        setTargetLanguage(language);

        TextTranslator translator = new TextTranslator();
        translator.setFromLanguage("English");
        translator.setToLanguage(targetLanguage);
        translator.translate(getNote());
    }

    @Override
    public void onInit(int status) {
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {

            }

            @Override
            public void onDone(String utteranceId) {
                inputNote.setVisibility(View.VISIBLE);
                ttsView.setVisibility(View.GONE);
            }

            @Override
            public void onError(String utteranceId) {

            }

            @Override
            public void onRangeStart(String utteranceId, int start, int end, int frame) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Spannable textHighlight = new SpannableString(getNote());
                        textHighlight.setSpan(new ForegroundColorSpan(Color.YELLOW), start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                        ttsView.setText(textHighlight);
                    }
                });
            }
        });
    }
}
